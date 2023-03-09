package org.olf

import org.olf.SerialRuleset
import org.olf.recurrence.recurrencePattern.RecurrencePattern
import org.olf.recurrence.Recurrence
import org.olf.recurrence.RecurrenceRule
import org.olf.recurrence.RulesetBindingUtilities

import com.k_int.okapi.OkapiTenantAwareController

import grails.rest.*
import grails.converters.*
import org.grails.web.json.JSONObject
import grails.gorm.transactions.Transactional
import grails.gorm.multitenancy.CurrentTenant
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

import java.util.regex.Pattern

@Slf4j
@CurrentTenant
class SerialRulesetController extends OkapiTenantAwareController<SerialRulesetController> {

  SerialRulesetController(){
    super(SerialRulesetController)
  }

  @Transactional
  def save () {
    JSONObject objToBind = getObjectToBind();
    SerialRuleset serialRuleset = RulesetBindingUtilities.bindSerialRuleset(objToBind)
    serialRuleset.save()
    respond serialRuleset
  }
}
