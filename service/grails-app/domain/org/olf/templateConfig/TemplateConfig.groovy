package org.olf.templateConfig

import org.olf.templateConfig.templateMetadataRule.ChronologyTemplateMetadataRule
import org.olf.templateConfig.templateMetadataRule.EnumerationTemplateMetadataRule

import org.olf.SerialRuleset

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class TemplateConfig implements MultiTenant<TemplateConfig> {
  String id
  SerialRuleset owner
  String templateString

  static hasMany = [
    chronologyRules: ChronologyTemplateMetadataRule,
    enumerationRules: EnumerationTemplateMetadataRule
  ]

  static belongsTo = [
    owner: SerialRuleset
 	]

  static mapping = {
    id column: 'tc_id', generator: 'uuid2', length: 36
    owner column: 'tc_owner_fk'
    version column: 'tc_version'
    templateString column: 'tc_template_string'
    chronologyRules cascade: 'all-delete-orphan', sort: 'index', order: 'asc'
    enumerationRules cascade: 'all-delete-orphan', sort: 'index', order: 'asc'
  }

  static constraints = {
    owner nullable: false
    chronologyRules nullable: true
    enumerationRules nullable: true
    templateString nullable: false
  }
}
