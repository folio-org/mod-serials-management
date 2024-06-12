package org.olf.startingValueMetadata

import org.olf.startingValueMetadata.startingValueMetadataFormat.StartingValueMetadataFormat

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
public class StartingValueMetadataFormatHelpers {

	private static GrailsWebDataBinder getDataBinder() {
		Holders.grailsApplication.mainContext.getBean(DataBindingUtils.DATA_BINDER_BEAN_NAME) as GrailsWebDataBinder
	}

	// Compile the regex once and reference statically.
	private static final Pattern RGX_RULE_TYPE_CLASS = Pattern.compile("_([a-z])")
	
	public static <T extends StartingValueMetadataFormat> T doFormatBinding( StartingValueMetadata obj, SimpleMapDataBindingSource source) {
		
		String formatString = null
		if ( source['startingValueMetadataFormat'] && RefdataValue.class.isAssignableFrom(source['startingValueMetadataFormat'].class)) {
			formatString = (source['startingValueMetadataFormat'] as RefdataValue).value;
		} else {
			formatString = source['startingValueMetadataFormat']?.toString();
		}

		// Do the string replacement.
		final String formatClassString = RGX_RULE_TYPE_CLASS.matcher(formatString)
				.replaceAll{ match -> match.group(1).toUpperCase() }

		final String formatClasspathString = "org.olf.startingValueMetadata.startingValueMetadataFormat.${formatClassString.capitalize()}SVMF"

		final Class<? extends StartingValueMetadataFormat> rc = Class.forName(formatClasspathString)

		final String currentId = source['format']?.getAt("id")?.toString()

		def rpApi = GormUtils.gormStaticApi(rc)

		T rp = currentId ? rpApi.get(currentId) : rpApi.create()

		dataBinder.bind( rp, new SimpleMapDataBindingSource(source['format'] as Map) )
		
		log.debug ('Binding Starting Value Metadata Format of format {} to Object {}', rp, obj)
		
		rp
	}
	
	
	public static Closure formatValidator = { StartingValueMetadataFormat value, StartingValueMetadata instance, Errors err ->
		validateFormat( value, instance, err )
	}
	
	private static void validateFormat ( StartingValueMetadataFormat value, StartingValueMetadata instance, Errors err ) {
		GormValidateable rpVal = value as GormValidateable
		
		if (!rpVal.validate(deepValidate: false)) { // Make sure this validation isn't fired again.
			
			Errors styleErrors = rpVal.getErrors()
			for( def error : styleErrors.allErrors) {
				err.rejectValue('format', error.code, error.arguments, error.defaultMessage)
			}
		}
	}
}
