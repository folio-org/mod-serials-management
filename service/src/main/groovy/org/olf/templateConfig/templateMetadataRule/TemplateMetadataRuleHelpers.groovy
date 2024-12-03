package org.olf.templateConfig.templateMetadataRule

import org.olf.templateConfig.templateMetadataRuleFormat.TemplateMetadataRuleFormat

import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource
import grails.util.Holders
import grails.validation.Validateable
import grails.web.databinding.DataBindingUtils
import grails.web.databinding.GrailsWebDataBinder
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.regex.Pattern

import org.codehaus.groovy.runtime.InvokerHelper
import org.grails.datastore.gorm.GormValidateable
import org.grails.datastore.mapping.dirty.checking.DirtyCheckable
import org.springframework.validation.Errors
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.utils.GormUtils

@CompileStatic
@Slf4j
public class TemplateMetadataRuleHelpers {

	private static GrailsWebDataBinder getDataBinder() {
		Holders.grailsApplication.mainContext.getBean(DataBindingUtils.DATA_BINDER_BEAN_NAME) as GrailsWebDataBinder
	}

	// Compile the regex once and reference statically.
	private static final Pattern RGX_RULE_FORMAT_CLASS = Pattern.compile("_([a-z])")
	
	public static <T extends TemplateMetadataRuleFormat> T doRuleFormatBinding( TemplateMetadataRule obj, SimpleMapDataBindingSource source) {

		String ruleFormatString = null
		if ( source['templateMetadataRuleFormat'] && RefdataValue.class.isAssignableFrom(source['templateMetadataRuleFormat'].class)) {
			ruleFormatString = (source['templateMetadataRuleFormat'] as RefdataValue).value;
		} else {
			ruleFormatString = source['templateMetadataRuleFormat']?.toString();
		}

		// Do the string replacement.
		final String ruleFormatClassString = RGX_RULE_FORMAT_CLASS.matcher(ruleFormatString)
				.replaceAll{ match -> match.group(1).toUpperCase() }

		final String ruleFormatClasspathString = "org.olf.templateConfig.templateMetadataRuleFormat.${ruleFormatClassString.capitalize()}TMRF"

		final Class<? extends TemplateMetadataRuleFormat> rc = Class.forName(ruleFormatClasspathString)

		final String currentId = source['ruleFormat']?.getAt("id")?.toString()

		def rpApi = GormUtils.gormStaticApi(rc)

		T rp = currentId ? rpApi.get(currentId) : rpApi.create()

		dataBinder.bind( rp, new SimpleMapDataBindingSource(source['ruleFormat'] as Map) )
		
		log.debug ('Binding Template Metadata Rule Type Format of type {} to Object {}', rp, obj)
		
		rp
	}
	
	
	public static Closure ruleFormatValidator = { TemplateMetadataRuleFormat value, TemplateMetadataRule instance, Errors err ->
		validateRuleFormat( value, instance, err )
	}
	
	private static void validateRuleFormat ( TemplateMetadataRuleFormat value, TemplateMetadataRule instance, Errors err ) {
		GormValidateable rpVal = value as GormValidateable
		
		if (!rpVal.validate(deepValidate: false)) { // Make sure this validation isn't fired again.
			
			Errors formatErrors = rpVal.getErrors()
			for( def error : formatErrors.allErrors) {
				err.rejectValue('ruleFormat', error.code, error.arguments, error.defaultMessage)
			}
		}
	}
}
