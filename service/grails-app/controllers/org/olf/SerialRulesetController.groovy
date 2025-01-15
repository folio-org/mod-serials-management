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

import static org.springframework.http.HttpStatus.*

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

  //Check to see if new ruleset is active, if it is, additionally set the current active ruleset to deprecated
  private void activeRulesetCheck(SerialRuleset ruleset) {
    if(ruleset?.rulesetStatus?.value == 'active'){
      String activeRulesetId = serialRulesetService.findActive(ruleset?.owner?.id)
      if(activeRulesetId){
        serialRulesetService.updateRulesetStatus(activeRulesetId, 'deprecated')
      }
    }
  }

  @Transactional
  def replaceAndDelete() {
    SerialRuleset.withTransaction {
      def data = getObjectToBind()
      // Existing ruleset
      String existingRulesetId = params.get("serialRulesetId") 
      SerialRuleset existing = queryForResource(existingRulesetId)
      // If existing ruleset doesnt exist, return error
      if (existing == null) {
        transactionStatus.setRollbackOnly()
        notFound()
        return
      }
      // Check to see if the existing ruleset has any predicted piece sets associated with it
      Integer pieceSetCount = serialRulesetService.countPieceSets(existingRulesetId)[0]
      if(pieceSetCount > 0){
        transactionStatus.setRollbackOnly()
        render status: METHOD_NOT_ALLOWED, text: "Cannot delete a serial ruleset which has been used to generate a predicted piece set"
        return
      }
      // New ruleset
      SerialRuleset ruleset = new SerialRuleset(data)
      activeRulesetCheck(ruleset)

      ruleset.save(failOnError: true)
      if(ruleset.hasErrors()) {
        transactionStatus.setRollbackOnly()
        respond ruleset.errors
      }
      // Finally delete the predicted piece set if we get this far and respond.
      deleteResource existing
      respond ruleset
    }
  }

  @Transactional
  def replaceAndDeprecate() {
    SerialRuleset.withTransaction {
      def data = getObjectToBind()
      // Existing ruleset
      String existingRulesetId = params.get("serialRulesetId") 
      SerialRuleset existing = queryForResource(existingRulesetId)
      // If existing ruleset doesnt exist, return error
      if (existing == null) {
        transactionStatus.setRollbackOnly()
        notFound()
        return
      }
      // New ruleset
      SerialRuleset ruleset = new SerialRuleset(data)
      //Check to see if new ruleset is active, if it is, additionally set the current active ruleset to deprecated
      activeRulesetCheck(ruleset)

      ruleset.save(failOnError: true)
      if(ruleset.hasErrors()) {
        transactionStatus.setRollbackOnly()
        respond ruleset.errors
      }
      // Finally deprecate existing ruleset
      serialRulesetService.updateRulesetStatus(existingRulesetId, 'deprecated')
      respond ruleset
    }
  }

  @Transactional
  def save() {
    SerialRuleset.withTransaction {
      def data = getObjectToBind()

      SerialRuleset ruleset = new SerialRuleset(data)
      activeRulesetCheck(ruleset)

      ruleset.save(failOnError: true)
      if(ruleset.hasErrors()) {
        transactionStatus.setRollbackOnly()
        respond ruleset.errors
      }
      respond ruleset
    }
  }
}
