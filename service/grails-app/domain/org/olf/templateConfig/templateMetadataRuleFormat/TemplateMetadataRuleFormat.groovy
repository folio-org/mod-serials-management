package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRule

import grails.gorm.MultiTenant

public abstract class TemplateMetadataRuleFormat implements MultiTenant<TemplateMetadataRuleFormat> {
  String id
  TemplateMetadataRule owner

  static mapping = {
    id column: 'tmrf_id', generator: 'uuid2', length: 36
    owner column: 'tmrf_owner_fk'
    version column: 'tmrf_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
