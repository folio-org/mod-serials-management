package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.ChronologyTemplateMetadataRule

import grails.gorm.MultiTenant

public abstract class ChronologyTemplateMetadataRuleFormat implements MultiTenant<ChronologyTemplateMetadataRuleFormat> {
  String id
  ChronologyTemplateMetadataRule owner

  static mapping = {
    id column: 'ctmrf_id', generator: 'uuid2', length: 36
    owner column: 'ctmrf_owner_fk'
    version column: 'ctmrf_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
