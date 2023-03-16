package org.olf

import org.olf.SerialRuleset
import org.olf.recurrence.recurrencePattern.RecurrencePattern
import org.olf.recurrence.Recurrence
import org.olf.recurrence.RecurrenceRule

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
class SerialRulesetController extends OkapiTenantAwareController<SerialRuleset> {

  SerialRulesetController(){
    super(SerialRuleset)
  }
}
