package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.EnumerationTemplateMetadataRule

import grails.gorm.MultiTenant

public abstract class EnumerationTemplateMetadataRuleFormat implements MultiTenant<EnumerationTemplateMetadataRuleFormat> {
  String id
  EnumerationTemplateMetadataRule owner

  static mapping = {
    id column: 'etmrf_id', generator: 'uuid2', length: 36
    owner column: 'etmrf_owner_fk'
    version column: 'etmrf_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
