package org.olf.templateConfig.templateMetadataRule

import org.olf.templateConfig.templateMetadataRuleFormat.TemplateMetadataRuleFormat
import org.olf.internalPiece.templateMetadata.ChronologyTemplateMetadata

import java.util.regex.Pattern

import java.time.LocalDate

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyTemplateMetadataRule extends TemplateMetadataRuleType implements MultiTenant<ChronologyTemplateMetadataRule> {

  @CategoryId(value="ChronologyTemplateMetadataRule.LabelFormat", defaultInternal=true)
  @Defaults(['Chronology Date', 'Chronology Month', 'Chronology Year'])
  RefdataValue templateMetadataRuleFormat

  // TODO Fix this
  @BindUsing({ TemplateMetadataRuleType obj, SimpleMapDataBindingSource source ->
		TemplateMetadataRuleTypeHelpers.doRuleFormatBinding(obj, source)
  })
  TemplateMetadataRuleFormat ruleFormat

  static hasOne = [
   	format: TemplateMetadataRuleFormat
  ]


  static mapping = {
    labelFormat column: 'lsc_label_format_fk'
    format cascade: 'all-delete-orphan'
  }

  static constraints = {
    labelFormat nullable: false
    format nullable: false, validator: TemplateMetadataRuleTypeHelpers.ruleFormatValidator
  }

  public static ChronologyTemplateMetadata handleStyle(ChronologyTemplateMetadataRule rule, LocalDate date, int index) {
    final Pattern RGX_RULE_FORMAT = Pattern.compile('_([a-z])')
    String formattedRuleFormat = RGX_RULE_FORMAT.matcher(rule?.style?.labelFormat?.value).replaceAll { match -> match.group(1).toUpperCase() }
    Class<? extends TemplateMetadataRuleFormat> lfc = Class.forName("org.olf.templateConfig.templateMetadataRuleFormat.${formattedRuleFormat.capitalize()}TMRF")
    return lfc.handleFormat(rule, date)
  }
}
