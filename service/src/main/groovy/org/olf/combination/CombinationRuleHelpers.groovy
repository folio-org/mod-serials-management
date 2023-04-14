package org.olf.combination

import org.olf.combination.combinationPattern.CombinationPattern

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
public class CombinationRuleHelpers {

	private static GrailsWebDataBinder getDataBinder() {
		Holders.grailsApplication.mainContext.getBean(DataBindingUtils.DATA_BINDER_BEAN_NAME) as GrailsWebDataBinder
	}

	// Compile the regex once and reference statically.
	private static final Pattern RGX_PATTERN_CLASS = Pattern.compile("_([a-z])")
	
	public static <T extends CombinationPattern> T doRulePatternBinding( CombinationRule obj, SimpleMapDataBindingSource source) {
		
		String patternTypeString = null
		if ( source['patternType'] && RefdataValue.class.isAssignableFrom(source['patternType'].class)) {
			patternTypeString = (source['patternType'] as RefdataValue).value;
		} else {
			patternTypeString = source['patternType']?.toString();
		}

		// Do the string replacement.
		final String patternClassString = RGX_PATTERN_CLASS.matcher(patternTypeString)
				.replaceAll{ match -> match.group(1).toUpperCase() }

		final String patternClasspathString = "org.olf.combination.combinationPattern.CombinationPattern${patternClassString.capitalize()}"

		final Class<? extends CombinationPattern> rc = Class.forName(patternClasspathString)

		final String currentId = source['pattern']?.getAt("id")?.toString()

		def rpApi = GormUtils.gormStaticApi(rc)

		T rp = currentId ? rpApi.get(currentId) : rpApi.create()

		dataBinder.bind( rp, new SimpleMapDataBindingSource(source['pattern'] as Map) )
		
		log.debug ('Binding Combination Pattern of type {} to Object {}', rp, obj)
		
		rp
	}
	
	
	public static Closure rulePatternValidator = { CombinationPattern value, CombinationRule instance, Errors err ->
		validateRulePattern( value, instance, err )
	}
	
	private static void validateRulePattern ( CombinationPattern value, CombinationRule instance, Errors err ) {
		GormValidateable rpVal = value as GormValidateable
		
		if (!rpVal.validate(deepValidate: false)) { // Make sure this validation isn't fired again.
			
			Errors paternErrors = rpVal.getErrors()
			for( def error : paternErrors.allErrors) {
				err.rejectValue('pattern', error.code, error.arguments, error.defaultMessage)
			}
		}
	}
}
