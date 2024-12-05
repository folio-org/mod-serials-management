package org.olf.templateConfig.templateMetadataRule

import org.olf.templateConfig.TemplateConfig
import org.olf.templateConfig.templateMetadataRuleFormat.EnumerationTemplateMetadataRuleFormat
import org.olf.internalPiece.templateMetadata.EnumerationUCTMT

import java.util.regex.Pattern

import java.time.LocalDate

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationTemplateMetadataRule implements MultiTenant<EnumerationTemplateMetadataRule> {
  String id
  TemplateConfig owner
  Integer index
  
  @CategoryId(value="EnumerationTemplateMetadataRule.TemplateMetadataRuleFormat", defaultInternal=true)
  // We setup these defaults within the HouseKeepingService in order to assign non-standard labels
  // @Defaults(['Enumeration Numeric', 'Enumeration Textual'])
  RefdataValue templateMetadataRuleFormat

  @BindUsing({ EnumerationTemplateMetadataRule obj, SimpleMapDataBindingSource source ->
		EnumerationTemplateMetadataRuleHelpers.doRuleFormatBinding(obj, source)
  })
  EnumerationTemplateMetadataRuleFormat ruleFormat

  static hasOne = [
   	ruleFormat: EnumerationTemplateMetadataRuleFormat
  ]

  static mapping = {
    id column: 'etmr_id', generator: 'uuid2', length: 36
    owner column: 'etmr_owner_fk'
    index column: 'etmr_index'
    version column: 'etmr_version'
    templateMetadataRuleFormat column: 'etmr_template_metadata_rule_format_fk'
    ruleFormat cascade: 'all-delete-orphan'
  }

  static constraints = {
    owner nullable: false
    index nullable: false
    templateMetadataRuleFormat nullable: false
    ruleFormat nullable: false, validator: EnumerationTemplateMetadataRuleHelpers.ruleFormatValidator
  }

  public static EnumerationUCTMT handleType(EnumerationTemplateMetadataRule rule, LocalDate date, int index, EnumerationUCTMT startingValues) {
    final Pattern RGX_RULE_FORMAT = Pattern.compile('_([a-z])')
    String ruleFormatClassString = RGX_RULE_FORMAT.matcher(rule?.templateMetadataRuleFormat?.value).replaceAll { match -> match.group(1).toUpperCase() }
    Class<? extends EnumerationTemplateMetadataRuleFormat> rfc = Class.forName("org.olf.templateConfig.templateMetadataRuleFormat.${ruleFormatClassString.capitalize()}TMRF")
    return rfc.handleFormat(rule, date, index, startingValues)
  }
}
