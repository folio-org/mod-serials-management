package org.olf.templateConfig.templateMetadataRule

import org.olf.templateConfig.templateMetadataRuleFormat.TemplateMetadataRuleFormat
import org.olf.internalPiece.templateMetadata.ChronologyUCTMT

import java.util.regex.Pattern

import java.time.LocalDate
import java.util.Locale

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyTemplateMetadataRule extends TemplateMetadataRuleType implements MultiTenant<ChronologyTemplateMetadataRule> {

  @CategoryId(value="ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat", defaultInternal=true)
  @Defaults(['Chronology Date', 'Chronology Month', 'Chronology Year'])
  RefdataValue templateMetadataRuleFormat

  String ruleLocale = 'en'

  @BindUsing({ TemplateMetadataRuleType obj, SimpleMapDataBindingSource source ->
		TemplateMetadataRuleTypeHelpers.doRuleFormatBinding(obj, source)
  })
  TemplateMetadataRuleFormat ruleFormat

  static hasOne = [
   	ruleFormat: TemplateMetadataRuleFormat
  ]


  static mapping = {
    templateMetadataRuleFormat column: 'ctmr_template_metadata_rule_format_fk'
    ruleLocale column: 'ctmr_rule_locale'
    ruleFormat cascade: 'all-delete-orphan'
  }

  static constraints = {
    templateMetadataRuleFormat nullable: false
    ruleLocale nullable: false
    ruleFormat nullable: false, validator: TemplateMetadataRuleTypeHelpers.ruleFormatValidator
  }

  public static ChronologyUCTMT handleType(TemplateMetadataRule rule, LocalDate date, int index) {
    final Pattern RGX_RULE_FORMAT = Pattern.compile('_([a-z])')
    String ruleFormatClassString = RGX_RULE_FORMAT.matcher(rule?.ruleType?.templateMetadataRuleFormat?.value).replaceAll { match -> match.group(1).toUpperCase() }
    Class<? extends TemplateMetadataRuleFormat> rfc = Class.forName("org.olf.templateConfig.templateMetadataRuleFormat.${ruleFormatClassString.capitalize()}TMRF")
    return rfc.handleFormat(rule, date, index)
  }
}
