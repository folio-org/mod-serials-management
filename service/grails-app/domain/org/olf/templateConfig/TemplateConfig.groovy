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
  // TODO Mayeb seprate into two seperate lists for enumeration and chronology
  ArrayList<TemplateMetadataRule> rules

  static belongsTo = [
    owner: SerialRuleset
 	]

  static hasMany = [
    rules : TemplateMetadataRule
  ]

  static mapping = {
          id column: 'l_id', generator: 'uuid2', length: 36
       owner column: 'l_owner_fk'
     version column: 'l_version'
      rules cascade: 'all-delete-orphan'
  }

    static constraints = {
         owner nullable: false
         rules nullable: false
    }
}
