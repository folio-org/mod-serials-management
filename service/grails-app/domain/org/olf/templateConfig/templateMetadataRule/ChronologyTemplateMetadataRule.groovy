package org.olf.templateConfig.templateMetadataRule

import org.olf.label.labelFormat.LabelFormat

import java.util.regex.Pattern

import java.time.LocalDate

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyTemplateMetadataRule extends TemplateMetadataRuleType implements MultiTenant<ChronologyTemplateMetadataRule> {

  @CategoryId(value="LabelStyleChronology.LabelFormat", defaultInternal=true)
  @Defaults(['Chronology Date', 'Chronology Month', 'Chronology Year'])
  RefdataValue labelFormat

  // TODO Fix this
  @BindUsing({ LabelStyle obj, SimpleMapDataBindingSource source ->
		LabelStyleHelpers.doStyleFormatBinding(obj, source)
  })
  LabelFormat format

  static hasOne = [
   	format: LabelFormat
  ]


  static mapping = {
      labelFormat column: 'lsc_label_format_fk'
      	  format cascade: 'all-delete-orphan'
  }

  static constraints = {
      labelFormat nullable: false
      format nullable: false, validator: LabelStyleHelpers.styleFormatValidator
  }

  public static Map handleStyle(LabelRule rule, LocalDate date, int index) {
    final Pattern RGX_PATTERN_TYPE = Pattern.compile('_([a-z])')
    String formattedLabelFormat = RGX_PATTERN_TYPE.matcher(rule?.style?.labelFormat?.value).replaceAll { match -> match.group(1).toUpperCase() }
    Class<? extends LabelFormat> lfc = Class.forName("org.olf.label.labelFormat.LabelFormat${formattedLabelFormat.capitalize()}")
    return lfc.handleFormat(rule, date)
  }
}
