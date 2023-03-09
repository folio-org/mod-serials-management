package org.olf

import org.olf.Serial
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
class SerialController extends OkapiTenantAwareController<Serial> {

  SerialController(){
    super(Serial)
  }

  @Transactional
  def save () {
    JSONObject objToBind = getObjectToBind();
    Serial serial = RulesetBindingUtilities.bindSerial(objToBind)
    serial.save()
    respond serial
  }
 }