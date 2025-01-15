package org.olf

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

@GrailsCompileStatic
class ModelRuleset extends RulesetOwner implements MultiTenant<ModelRuleset> {

  String id
  String name
  String description
  String exampleLabel

  SerialRuleset serialRuleset

  @CategoryId(defaultInternal=true)
  @Defaults(['Active', 'Closed'])
  RefdataValue modelRulesetStatus

  static hasOne = [
    serialRuleset: SerialRuleset
  ]

  static mappedBy = [
    serialRuleset: 'owner',
  ]

  static mapping = {
    id column: 'mr_id', generator: 'uuid2', length: 36
    name column: 'mr_name'
    description column: 'mr_description'
    exampleLabel column: 'mr_example_label'
    modelRulesetStatus column: 'mr_ruleset_template_status'
    serialRuleset cascade: 'all-delete-orphan'
  }
  
  static constraints = {
    name nullable: false
    description nullable: true
    // TODO Should example be false?
    exampleLabel nullable: true
    modelRulesetStatus nullable: false
  }   
}
