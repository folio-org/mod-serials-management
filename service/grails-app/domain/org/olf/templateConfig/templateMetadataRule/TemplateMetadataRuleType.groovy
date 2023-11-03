package org.olf.templateConfig.templateMetadataRule

import grails.gorm.MultiTenant

public abstract class TemplateMetadataRuleType implements MultiTenant<TemplateMetadataRuleType> {
  String id
  TemplateMetadataRule owner

  static mapping = {
         id column: 'ls_id', generator: 'uuid2', length: 36
      owner column: 'ls_owner_fk'
    version column: 'ls_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
