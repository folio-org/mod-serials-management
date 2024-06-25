package org.olf.internalPiece.templateMetadata

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
public class UserConfiguredTemplateMetadataHelpers {

	private static GrailsWebDataBinder getDataBinder() {
		Holders.grailsApplication.mainContext.getBean(DataBindingUtils.DATA_BINDER_BEAN_NAME) as GrailsWebDataBinder
	}

	// Compile the regex once and reference statically.
	private static final Pattern RGX_RULE_TYPE_CLASS = Pattern.compile("_([a-z])")
	
	public static <T extends UserConfiguredTemplateMetadataType> T doMetadataTypeBinding( UserConfiguredTemplateMetadata obj, SimpleMapDataBindingSource source) {
		
		String formatString = null
		if ( source['userConfiguredTemplateMetadataType'] && RefdataValue.class.isAssignableFrom(source['userConfiguredTemplateMetadataType'].class)) {
			formatString = (source['userConfiguredTemplateMetadataType'] as RefdataValue).value;
		} else {
			formatString = source['userConfiguredTemplateMetadataType']?.toString();
		}

		// Do the string replacement.
		final String metadataTypeClassString = RGX_RULE_TYPE_CLASS.matcher(formatString)
				.replaceAll{ match -> match.group(1).toUpperCase() }

		final String metadataTypeClasspathString = "org.olf.internalPiece.templateMetadata.${metadataTypeClassString.capitalize()}UCTMT"

		final Class<? extends UserConfiguredTemplateMetadataType> rc = Class.forName(metadataTypeClasspathString)

		final String currentId = source['metadataType']?.getAt("id")?.toString()

		def rpApi = GormUtils.gormStaticApi(rc)

		T rp = currentId ? rpApi.get(currentId) : rpApi.create()

		dataBinder.bind( rp, new SimpleMapDataBindingSource(source['metadataType'] as Map) )
		
		log.debug ('Binding User Configured Template Metadata Type of type {} to Object {}', rp, obj)
		
		rp
	}
	
	
	public static Closure metadataTypeValidator = { UserConfiguredTemplateMetadataType value, UserConfiguredTemplateMetadata instance, Errors err ->
		validateMetadataType( value, instance, err )
	}
	
	private static void validateMetadataType ( UserConfiguredTemplateMetadataType value, UserConfiguredTemplateMetadata instance, Errors err ) {
		GormValidateable rpVal = value as GormValidateable
		
		if (!rpVal.validate(deepValidate: false)) { // Make sure this validation isn't fired again.
			
			Errors styleErrors = rpVal.getErrors()
			for( def error : styleErrors.allErrors) {
				err.rejectValue('metadataType', error.code, error.arguments, error.defaultMessage)
			}
		}
	}
}
