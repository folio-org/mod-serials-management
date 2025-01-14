package org.olf

import org.olf.recurrence.Recurrence

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import org.springframework.validation.Errors

import groovy.sql.Sql


// ModelRuleset

@GrailsCompileStatic
class RulesetTemplate extends RulesetOwner implements MultiTenant<RulesetTemplate> {

  String id
  String name
  String description
  String exampleLabel

  SerialRuleset serialRuleset

  @CategoryId(defaultInternal=true)
  @Defaults(['Active', 'Closed'])
  RefdataValue rulesetTemplateStatus

  static hasOne = [
    serialRuleset: SerialRuleset
  ]

  static mappedBy = [
    serialRuleset: 'owner',
  ]

  static mapping = {
    id column: 'rt_id', generator: 'uuid2', length: 36
    name column: 'rt_name'
    description column: 'rt_description'
    exampleLabel column: 'rt_example_label'
    rulesetTemplateStatus column: 'rt_ruleset_template_status'
    serialRuleset cascade: 'all-delete-orphan'
  }
  
  static constraints = {
    name nullable: false
    description nullable: true
    // TODO Should example be false?
    exampleLabel nullable: true
    rulesetTemplateStatus nullable: false
  }   
}
