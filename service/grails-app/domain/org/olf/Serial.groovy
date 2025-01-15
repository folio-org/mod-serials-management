package org.olf

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import org.springframework.validation.Errors

@GrailsCompileStatic
class Serial extends RulesetOwner implements MultiTenant<Serial> {

  String id
  String description

  @CategoryId(defaultInternal=true)
  @Defaults(['Active', 'Closed'])
  RefdataValue serialStatus

  static hasMany = [
    notes : SerialNote,
    serialRulesets: SerialRuleset
  ]

  static hasOne = [
    orderLine: SerialOrderLine,
  ]

  static mappedBy = [
    orderLine: 'owner',
    serialRulesets: 'owner'
  ]

  static mapping = {
    id column: 's_id', generator: 'uuid2', length: 36
    serialStatus column: 's_serial_status'
    description column: 's_description'

    orderLine cascade: 'all-delete-orphan'
    serialRulesets cascade: 'all-delete-orphan'
    notes cascade: 'all-delete-orphan'
  }
  
  static constraints = {
    orderLine nullable: true
    serialStatus nullable: true
    description nullable: true
    serialRulesets nullable: true, validator: { Collection<SerialRuleset> s_rulesets, _obj, Errors errors ->
      int activeCount = ((s_rulesets?.findAll({ SerialRuleset ruleset -> ruleset.rulesetStatus.value == 'active' })?.size()) ?: 0)
      if (activeCount > 1) {
        errors.rejectValue('serialRulesets', 'only.one.active.ruleset', [activeCount] as Object[], 'Incorrect ruleset statuses')
      }
    }
  }   
}
