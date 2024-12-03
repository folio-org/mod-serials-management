package org.olf.templateConfig.templateMetadataRule

import org.olf.templateConfig.TemplateConfig
import org.olf.templateConfig.templateMetadataRuleFormat.ChronologyTemplateMetadataRuleFormat
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

public class ChronologyTemplateMetadataRule implements MultiTenant<ChronologyTemplateMetadataRule> {
  String id
  TemplateConfig owner
  Integer index

  @CategoryId(value="ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat", defaultInternal=true)
  // We setup these defaults within the HouseKeepingService in order to assign non-standard labels
  // @Defaults(['Chronology Date', 'Chronology Month', 'Chronology Year'])
  RefdataValue templateMetadataRuleFormat

  String ruleLocale = 'en'

  @BindUsing({ ChronologyTemplateMetadataRule obj, SimpleMapDataBindingSource source ->
		ChronologyTemplateMetadataRuleHelpers.doRuleFormatBinding(obj, source)
  })
  ChronologyTemplateMetadataRuleFormat ruleFormat

  static hasOne = [
   	ruleFormat: ChronologyTemplateMetadataRuleFormat
  ]


  static mapping = {
    id column: 'ctmr_id', generator: 'uuid2', length: 36
    owner column: 'ctmr_owner_fk'
    index column: 'ctmr_index'
    version column: 'ctmr_version'
    templateMetadataRuleFormat column: 'ctmr_template_metadata_rule_format_fk'
    ruleLocale column: 'ctmr_rule_locale'
    ruleFormat cascade: 'all-delete-orphan'
  }

  static constraints = {
    owner nullable: false
    index nullable: false
    templateMetadataRuleFormat nullable: false
    ruleLocale nullable: false
    ruleFormat nullable: false, validator: ChronologyTemplateMetadataRuleHelpers.ruleFormatValidator
  }

  public static ChronologyUCTMT handleType(ChronologyTemplateMetadataRule rule, LocalDate date, int index) {
    final Pattern RGX_RULE_FORMAT = Pattern.compile('_([a-z])')
    String ruleFormatClassString = RGX_RULE_FORMAT.matcher(rule?.ruleType?.templateMetadataRuleFormat?.value).replaceAll { match -> match.group(1).toUpperCase() }
    Class<? extends ChronologyTemplateMetadataRuleFormat> rfc = Class.forName("org.olf.templateConfig.templateMetadataRuleFormat.${ruleFormatClassString.capitalize()}TMRF")
    return rfc.handleFormat(rule, date, index)
  }
}
