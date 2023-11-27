package org.olf.templateConfig.templateMetadataRule

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRuleType

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
	private static final Pattern RGX_RULE_TYPE_CLASS = Pattern.compile("_([a-z])")
	
	public static <T extends TemplateMetadataRuleType> T doRuleTypeBinding( TemplateMetadataRule obj, SimpleMapDataBindingSource source) {
		
		String ruleTypeString = null
		if ( source['templateMetadataRuleType'] && RefdataValue.class.isAssignableFrom(source['templateMetadataRuleType'].class)) {
			ruleTypeString = (source['templateMetadataRuleType'] as RefdataValue).value;
		} else {
			ruleTypeString = source['templateMetadataRuleType']?.toString();
		}

		// Do the string replacement.
		final String ruleTypeClassString = RGX_RULE_TYPE_CLASS.matcher(ruleTypeString)
				.replaceAll{ match -> match.group(1).toUpperCase() }

		final String ruleTypeClasspathString = "org.olf.templateConfig.templateMetadataRule.${ruleTypeClassString.capitalize()}TemplateMetadataRule"

		final Class<? extends TemplateMetadataRuleType> rc = Class.forName(ruleTypeClasspathString)

		final String currentId = source['ruleType']?.getAt("id")?.toString()

		def rpApi = GormUtils.gormStaticApi(rc)

		T rp = currentId ? rpApi.get(currentId) : rpApi.create()

		dataBinder.bind( rp, new SimpleMapDataBindingSource(source['ruleType'] as Map) )
		
		log.debug ('Binding Template Metadata Rule Type of type {} to Object {}', rp, obj)
		
		rp
	}
	
	
	public static Closure ruleTypeValidator = { TemplateMetadataRuleType value, TemplateMetadataRule instance, Errors err ->
		validateRuleType( value, instance, err )
	}
	
	private static void validateRuleType ( TemplateMetadataRuleType value, TemplateMetadataRule instance, Errors err ) {
		GormValidateable rpVal = value as GormValidateable
		
		if (!rpVal.validate(deepValidate: false)) { // Make sure this validation isn't fired again.
			
			Errors styleErrors = rpVal.getErrors()
			for( def error : styleErrors.allErrors) {
				err.rejectValue('ruleType', error.code, error.arguments, error.defaultMessage)
			}
		}
	}
}
