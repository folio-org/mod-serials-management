package org.olf.templateConfig.templateMetadataRule

import grails.gorm.MultiTenant

import org.olf.templateConfig.TemplateConfig

public abstract class TemplateMetadataRule implements MultiTenant<TemplateMetadataRule> {
  String id
  TemplateConfig owner
  Integer index

  static mapping = {
    id column: 'tmr_id', generator: 'uuid2', length: 36
    owner column: 'tmr_owner_fk'
    index column: 'tmr_index'
    version column: 'tmr_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
