package org.olf.label.labelStyle

import org.olf.label.labelFormat.LabelFormat

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
public class LabelStyleHelpers {

	private static GrailsWebDataBinder getDataBinder() {
		Holders.grailsApplication.mainContext.getBean(DataBindingUtils.DATA_BINDER_BEAN_NAME) as GrailsWebDataBinder
	}

	// Compile the regex once and reference statically.
	private static final Pattern RGX_PATTERN_CLASS = Pattern.compile("_([a-z])")
	
	public static <T extends LabelFormat> T doStyleFormatBinding( LabelStyle obj, SimpleMapDataBindingSource source) {

		String labelFormatString = null
		if ( source['labelFormat'] && RefdataValue.class.isAssignableFrom(source['labelFormat'].class)) {
			labelFormatString = (source['labelFormat'] as RefdataValue).value;
		} else {
			labelFormatString = source['labelFormat']?.toString();
		}

		// Do the string replacement.
		final String formatClassString = RGX_PATTERN_CLASS.matcher(labelFormatString)
				.replaceAll{ match -> match.group(1).toUpperCase() }

		final String formatClasspathString = "org.olf.label.labelFormat.LabelFormat${formatClassString.capitalize()}"

		final Class<? extends LabelFormat> rc = Class.forName(formatClasspathString)

		final String currentId = source['format']?.getAt("id")?.toString()

		def rpApi = GormUtils.gormStaticApi(rc)

		T rp = currentId ? rpApi.get(currentId) : rpApi.create()

		dataBinder.bind( rp, new SimpleMapDataBindingSource(source['format'] as Map) )
		
		log.debug ('Binding Label Format of type {} to Object {}', rp, obj)
		
		rp
	}
	
	
	public static Closure styleFormatValidator = { LabelFormat value, LabelStyle instance, Errors err ->
		validateRuleStyle( value, instance, err )
	}
	
	private static void validateRuleStyle ( LabelFormat value, LabelStyle instance, Errors err ) {
		GormValidateable rpVal = value as GormValidateable
		
		if (!rpVal.validate(deepValidate: false)) { // Make sure this validation isn't fired again.
			
			Errors formatErrors = rpVal.getErrors()
			for( def error : formatErrors.allErrors) {
				err.rejectValue('format', error.code, error.arguments, error.defaultMessage)
			}
		}
	}
}
