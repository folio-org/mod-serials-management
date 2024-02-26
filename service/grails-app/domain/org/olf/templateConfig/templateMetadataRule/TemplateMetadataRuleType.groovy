package org.olf.templateConfig.templateMetadataRule

import grails.gorm.MultiTenant

public abstract class TemplateMetadataRuleType implements MultiTenant<TemplateMetadataRuleType> {
  String id
  TemplateMetadataRule owner

  static mapping = {
    id column: 'tmrt_id', generator: 'uuid2', length: 36
    owner column: 'tmrt_owner_fk'
    version column: 'tmrt_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
