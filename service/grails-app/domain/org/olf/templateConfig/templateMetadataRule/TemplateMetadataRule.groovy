package org.olf.templateConfig.templateMetadataRule

import org.olf.templateConfig.TemplateConfig

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class TemplateMetadataRule implements MultiTenant<TemplateMetadataRule> {
  String id
  TemplateConfig owner

  @CategoryId(value="TemplateMetadataRule.TemplateMetadataRuleType", defaultInternal=true)
  @Defaults(['Chronology', 'Enumeration'])
  RefdataValue templateMetadataRuleType 

  @BindUsing({ TemplateMetadataRule obj, SimpleMapDataBindingSource source ->
		TemplateMetadataRuleHelpers.doRuleTypeBinding(obj, source)
  })
  TemplateMetadataRuleType ruleType

	static belongsTo = [
    owner : TemplateConfig
  ]

	static hasOne = [
   	ruleType: TemplateMetadataRuleType
  ]

	static mapping = {
    id column: 'tmr_id', generator: 'uuid2', length: 36
    owner column: 'tmr_owner_fk'
    version column: 'tmr_version'
    templateMetadataRuleType column: 'tmr_template_metadata_rule_type_fk'
	  ruleType cascade: 'all-delete-orphan'
  }

  static constraints = {
    owner nullable: false
    templateMetadataRuleType nullable: false
    ruleType nullable: false, validator: TemplateMetadataRuleHelpers.ruleTypeValidator
  }
}
