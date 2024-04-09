package org.olf.templateConfig.templateMetadataRule

import org.olf.templateConfig.templateMetadataRuleFormat.TemplateMetadataRuleFormat
import org.olf.internalPiece.templateMetadata.EnumerationTemplateMetadata

import java.util.regex.Pattern

import java.time.LocalDate

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationTemplateMetadataRule extends TemplateMetadataRuleType implements MultiTenant<EnumerationTemplateMetadataRule> {
  @CategoryId(value="EnumerationTemplateMetadataRule.TemplateMetadataRuleFormat", defaultInternal=true)
  @Defaults(['Enumeration Numeric', 'Enumeration Textual'])
  RefdataValue templateMetadataRuleFormat

  @BindUsing({ TemplateMetadataRuleType obj, SimpleMapDataBindingSource source ->
		TemplateMetadataRuleTypeHelpers.doRuleFormatBinding(obj, source)
  })
  TemplateMetadataRuleFormat ruleFormat

  static hasOne = [
   	ruleFormat: TemplateMetadataRuleFormat
  ]

  static mapping = {
    templateMetadataRuleFormat column: 'etmr_template_metadata_rule_format_fk'
    ruleFormat cascade: 'all-delete-orphan'
  }

  static constraints = {
    templateMetadataRuleFormat nullable: false
    ruleFormat nullable: false, validator: TemplateMetadataRuleTypeHelpers.ruleFormatValidator
  }

  public static EnumerationTemplateMetadata handleType(TemplateMetadataRule rule, LocalDate date, int index) {
    final Pattern RGX_RULE_FORMAT = Pattern.compile('_([a-z])')
    String ruleFormatClassString = RGX_RULE_FORMAT.matcher(rule?.ruleType?.templateMetadataRuleFormat?.value).replaceAll { match -> match.group(1).toUpperCase() }
    println(ruleFormatClassString)
    println(rule?.index)
    Class<? extends TemplateMetadataRuleFormat> rfc = Class.forName("org.olf.templateConfig.templateMetadataRuleFormat.${ruleFormatClassString.capitalize()}TMRF")
    return rfc.handleFormat(rule, date, index)
  }
}
