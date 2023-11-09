package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRuleType

import grails.gorm.MultiTenant

public abstract class TemplateMetadataRuleFormat implements MultiTenant<TemplateMetadataRuleFormat> {
  String id
  TemplateMetadataRuleType owner

  static mapping = {
    id column: 'lf_id', generator: 'uuid2', length: 36
    owner column: 'lf_owner_fk'
    version column: 'lf_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
