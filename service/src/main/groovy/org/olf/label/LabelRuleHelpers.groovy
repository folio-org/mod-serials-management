package org.olf.label

import org.olf.label.labelStyle.LabelStyle

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
public class LabelRuleHelpers {

	private static GrailsWebDataBinder getDataBinder() {
		Holders.grailsApplication.mainContext.getBean(DataBindingUtils.DATA_BINDER_BEAN_NAME) as GrailsWebDataBinder
	}

	// Compile the regex once and reference statically.
	private static final Pattern RGX_PATTERN_CLASS = Pattern.compile("_([a-z])")
	
	public static <T extends LabelStyle> T doRuleStyleBinding( LabelRule obj, SimpleMapDataBindingSource source) {
		
		String labelStyleString = null
		if ( source['labelStyle'] && RefdataValue.class.isAssignableFrom(source['labelStyle'].class)) {
			labelStyleString = (source['labelStyle'] as RefdataValue).value;
		} else {
			labelStyleString = source['labelStyle']?.toString();
		}

		// Do the string replacement.
		final String styleClassString = RGX_PATTERN_CLASS.matcher(labelStyleString)
				.replaceAll{ match -> match.group(1).toUpperCase() }

		final String styleClasspathString = "org.olf.label.labelStyle.LabelStyle${styleClassString.capitalize()}"

		final Class<? extends LabelStyle> rc = Class.forName(styleClasspathString)

		final String currentId = source['style']?.getAt("id")?.toString()

		def rpApi = GormUtils.gormStaticApi(rc)

		T rp = currentId ? rpApi.get(currentId) : rpApi.create()

		dataBinder.bind( rp, new SimpleMapDataBindingSource(source['style'] as Map) )
		
		log.debug ('Binding Label Style of type {} to Object {}', rp, obj)
		
		rp
	}
	
	
	public static Closure ruleStyleValidator = { LabelStyle value, LabelRule instance, Errors err ->
		validateRuleStyle( value, instance, err )
	}
	
	private static void validateRuleStyle ( LabelStyle value, LabelRule instance, Errors err ) {
		GormValidateable rpVal = value as GormValidateable
		
		if (!rpVal.validate(deepValidate: false)) { // Make sure this validation isn't fired again.
			
			Errors styleErrors = rpVal.getErrors()
			for( def error : styleErrors.allErrors) {
				err.rejectValue('style', error.code, error.arguments, error.defaultMessage)
			}
		}
	}
}
