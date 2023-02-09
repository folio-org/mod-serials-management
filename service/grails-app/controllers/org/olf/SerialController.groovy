package org.olf

import org.olf.Serial
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

@Slf4j
@CurrentTenant
class SerialController extends OkapiTenantAwareController<Serial> {

  SerialController(){
    super(Serial)
  }

  @Transactional
  def save () {
    JSONObject objToBind = getObjectToBind();
    Set serialObjKeys = new HashSet(objToBind.keySet())
    // Removing recurrence from serial object
    serialObjKeys.remove('recurrence')
    String[] serialKeyArray = serialObjKeys.toArray(new String[serialObjKeys.size()]);
    // Binding to new serial object

    JSONObject serialObj = new JSONObject(objToBind, serialKeyArray)
    Serial serial = new Serial()
    bindData serial, serialObj

    log.debug("serial: ${serial}")
    // serial.save()
    // respond serial



    log.debug("objToBind: ${objToBind}")
    JSONObject orginalRecurrenceObj = objToBind.recurrence
    Set recurrenceObjKeys = new HashSet(orginalRecurrenceObj.keySet())
    // Removing recurrence from serial object
    recurrenceObjKeys.remove('rules')
    String[] recurrenceKeyArray = recurrenceObjKeys.toArray(new String[recurrenceObjKeys.size()]);
    // Binding to new serial object

    JSONObject recurrenceObj = new JSONObject(orginalRecurrenceObj, recurrenceKeyArray)
    Recurrence recurrence = new Recurrence()
    bindData recurrence, recurrenceObj
    serial.recurrence = recurrence
    // serial.save()
    // respond serial




    log.debug(recurrence.toString())
    orginalRecurrenceObj.rules.each { JSONObject ru ->

      Set ruleObjKeys = new HashSet(ru.keySet())
      // Removing recurrence from serial object
      ruleObjKeys.remove('pattern')
      String[] ruleKeyArray = ruleObjKeys.toArray(new String[ruleObjKeys.size()]);
      // Binding to new serial object

      JSONObject ruleObj = new JSONObject(ru, ruleKeyArray)
      RecurrenceRule recurrenceRule = new RecurrenceRule()
      bindData recurrenceRule, ruleObj

      final Class<? extends RecurrencePattern> rc = Class.forName("org.olf.recurrence.recurrencePattern.RecurrencePatternMonthWeekday")
      RecurrencePattern rp = rc.newInstance()
      JSONObject patternObj = new JSONObject(ru.pattern)
      patternObj.owner = recurrenceRule

      bindData rp, patternObj
      //Validate here
      recurrenceRule.pattern = rp
      recurrence.addToRules(recurrenceRule)
    }
    // log.debug(rp.toString())
    serial.save()
    respond serial
 }

}
