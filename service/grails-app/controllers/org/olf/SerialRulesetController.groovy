package org.olf

import org.olf.SerialRuleset
import org.olf.SerialRulesetService

import org.olf.recurrence.Recurrence

import com.k_int.okapi.OkapiTenantAwareController
import com.k_int.web.toolkit.refdata.RefdataValue

import grails.rest.*
import grails.converters.*
import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.transactions.Transactional

import groovy.util.logging.Slf4j

import java.util.regex.Pattern

@Slf4j
@CurrentTenant
class SerialRulesetController extends OkapiTenantAwareController<SerialRuleset> {

  SerialRulesetService serialRulesetService

  SerialRulesetController(){
    super(SerialRuleset)
  }

  def draftRuleset() {
    String serialRulesetId = params.get("serialRulesetId")
    SerialRuleset ruleset = serialRulesetService.updateRulesetStatus(serialRulesetId, 'draft')

    respond ruleset
  }

  def deprecateRuleset() {
    String serialRulesetId = params.get("serialRulesetId")
    SerialRuleset ruleset = serialRulesetService.updateRulesetStatus(serialRulesetId, 'deprecated')

    respond ruleset
  }

  def activateRuleset() {
    String serialRulesetId = params.get("serialRulesetId") 
    SerialRuleset ruleset = SerialRuleset.findById(serialRulesetId)

    String activeRulesetId = serialRulesetService.findActive(ruleset?.owner?.id)
    if(activeRulesetId){
      serialRulesetService.updateRulesetStatus(activeRulesetId, 'deprecated')
    }

    SerialRuleset result = serialRulesetService.updateRulesetStatus(serialRulesetId, 'active')
    respond result
  }

  def replaceAndDelete() {
    SerialRuleset.withTransaction {
      def data = getObjectToBind()
      SerialRuleset ruleset = new SerialRuleset(data)
      if(ruleset?.rulesetStatus?.value == 'active'){
        String activeRulesetId = serialRulesetService.findActive(ruleset?.owner?.id)
        if(activeRulesetId){
          serialRulesetService.updateRulesetStatus(activeRulesetId, 'deprecated')
        }
      }
      ruleset.save(failOnError: true)
      if(ruleset.hasErrors()) {
        transactionStatus.setRollbackOnly()
        respond ruleset.errors
      }
      respond ruleset
    }
  }

  def replaceAndDeprecate() {
    SerialRuleset.withTransaction {
      def data = getObjectToBind()
      SerialRuleset ruleset = new SerialRuleset(data)
      if(ruleset?.rulesetStatus?.value == 'active'){
        String activeRulesetId = serialRulesetService.findActive(ruleset?.owner?.id)
        if(activeRulesetId){
          serialRulesetService.updateRulesetStatus(activeRulesetId, 'deprecated')
        }
      }
      ruleset.save(failOnError: true)
      if(ruleset.hasErrors()) {
        transactionStatus.setRollbackOnly()
        respond ruleset.errors
      }
      respond ruleset
    }
  }

  @Transactional
  def save() {
    SerialRuleset.withTransaction {
      def data = getObjectToBind()
      SerialRuleset ruleset = new SerialRuleset(data)
      if(ruleset?.rulesetStatus?.value == 'active'){
        String activeRulesetId = serialRulesetService.findActive(ruleset?.owner?.id)
        if(activeRulesetId){
          serialRulesetService.updateRulesetStatus(activeRulesetId, 'deprecated')
        }
      }
      ruleset.save(failOnError: true)
      if(ruleset.hasErrors()) {
        transactionStatus.setRollbackOnly()
        respond ruleset.errors
      }
      respond ruleset
    }
  }
}
