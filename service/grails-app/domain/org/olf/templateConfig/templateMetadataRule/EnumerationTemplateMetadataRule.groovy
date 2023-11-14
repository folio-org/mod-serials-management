package org.olf.templateConfig.templateMetadataRule

import org.olf.templateConfig.templateMetadataRuleFormat.TemplateMetadataRuleFormat
import org.olf.internalPiece.templateMetadata.EnumerationTemplateMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationTemplateMetadataRule extends TemplateMetadataRuleType implements MultiTenant<EnumerationTemplateMetadataRule> {
  @CategoryId(value="EnumerationTemplateMetadataRule.TemplateMetadataRuleFormat", defaultInternal=true)
  @Defaults(['Enumeration Numeric', 'Enumeration Cyclical'])
  RefdataValue templateMetadataRuleFormat

  @BindUsing({ TemplateMetadataRuleType obj, SimpleMapDataBindingSource source ->
		TemplateMetadataRuleTypeHelpers.doRuleFormatBinding(obj, source)
  })
  TemplateMetadataRuleFormat ruleFormat

  static hasOne = [
   	ruleFormat: TemplateMetadataRuleFormat
  ]

  static mapping = {
    templateMetadataRuleFormat column: 'lsc_label_format_fk'
    ruleFormat cascade: 'all-delete-orphan'
  }

  static constraints = {
    templateMetadataRuleFormat nullable: false
    ruleFormat nullable: false, validator: TemplateMetadataRuleTypeHelpers.ruleFormatValidator
  }

  public static EnumerationTemplateMetadata handleStyle(TemplateMetadataRule rule, LocalDate date, int index) {
    final Pattern RGX_RULE_FORMAT = Pattern.compile('_([a-z])')
    String ruleFormatClassString = RGX_RULE_FORMAT.matcher(rule?.ruleType?.templateMetadataRuleFormat?.value).replaceAll { match -> match.group(1).toUpperCase() }
    Class<? extends TemplateMetadataRuleFormat> lfc = Class.forName("org.olf.templateConfig.templateMetadataRuleFormat.${ruleFormatClassString.capitalize()}TMRF")
    return lfc.handleFormat(rule, date)
  }
}
