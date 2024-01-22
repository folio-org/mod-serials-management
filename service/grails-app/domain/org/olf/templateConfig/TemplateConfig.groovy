package org.olf.templateConfig

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRule
import org.olf.SerialRuleset

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class TemplateConfig implements MultiTenant<TemplateConfig> {
  String id
  SerialRuleset owner
  String templateString
  // TODO Maybe seprate into two seperate lists for enumeration and chronology

  static hasMany = [
    rules: TemplateMetadataRule
  ]

  static belongsTo = [
    owner: SerialRuleset
 	]

  static mapping = {
    id column: 'tc_id', generator: 'uuid2', length: 36
    owner column: 'tc_owner_fk'
    version column: 'tc_version'
    templateString column: 'tc_template_string'
    rules cascade: 'all-delete-orphan', sort: 'index', order: 'asc'
  }

  static constraints = {
    owner nullable: false
    rules nullable: true
    templateString nullable: false
  }
}
