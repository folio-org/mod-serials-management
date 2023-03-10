package org.olf.recurrence

import org.olf.Serial
import org.olf.SerialRuleset
import org.olf.recurrence.recurrencePattern.RecurrencePattern
import org.olf.recurrence.Recurrence
import org.olf.recurrence.RecurrenceRule

import grails.util.Holders
import grails.web.databinding.DataBindingUtils
import grails.web.databinding.GrailsWebDataBinder
import grails.databinding.SimpleMapDataBindingSource
import groovy.transform.CompileStatic
import org.grails.web.json.JSONObject
import grails.gorm.transactions.Transactional

import java.util.regex.Pattern

class RulesetBindingUtilities {

  private static GrailsWebDataBinder getDataBinder() {
    Holders.grailsApplication.mainContext.getBean(DataBindingUtils.DATA_BINDER_BEAN_NAME)
  }

  static SerialRuleset bindSerialRuleset (JSONObject serialRulesetJson) {
    SerialRuleset serialRuleset = new SerialRuleset()

    if(serialRulesetJson?.owner?.id){
      Serial ownerSerial = Serial.get(serialRulesetJson.owner.id);
      serialRuleset.owner = ownerSerial
    }

    if(serialRulesetJson.recurrence){
      // We need to do special logic for recurrence rules so seperate it out
      Set recurrenceObjKeys = new HashSet(serialRulesetJson.recurrence.keySet())

      // Removing rules from original recurrence object
      recurrenceObjKeys.remove('rules')
      String[] recurrenceKeyArray = recurrenceObjKeys.toArray(new String[recurrenceObjKeys.size()]);

      // Binding to new serial object
      JSONObject recurrenceObj = new JSONObject(serialRulesetJson.recurrence, recurrenceKeyArray)
      Recurrence recurrence = new Recurrence()
      dataBinder.bind(recurrence, new SimpleMapDataBindingSource(recurrenceObj))
      
      // Add recurrence without rules to newly created serial
      // FIXME This feels wrong... but it works
      recurrence.owner = serialRuleset
      serialRuleset.recurrence = recurrence

      serialRulesetJson.recurrence.rules.each { JSONObject ru ->

        Set ruleObjKeys = new HashSet(ru.keySet())

        // Removing pattern from each rule within the original recurrence rules
        ruleObjKeys.remove('pattern')
        String[] ruleKeyArray = ruleObjKeys.toArray(new String[ruleObjKeys.size()]);

        // Binding to rule without pattern to new recurrence rule
        JSONObject ruleObj = new JSONObject(ru, ruleKeyArray)
        RecurrenceRule recurrenceRule = new RecurrenceRule()
        dataBinder.bind(recurrenceRule, new SimpleMapDataBindingSource(ruleObj))

        // Parse the pattern_type value to RecurrencePattern class i.e month_date -> RecurrencePatternMonthDate
        // THIS ASSUMES THE CONVENTION OF THE CLASS BEING NAMED RecurrencePattern<exact name of patternType value in CapitalCase>
        String patternClassString = Pattern.compile("_([a-z])").matcher(recurrenceRule.patternType.value).replaceAll{match -> match.group(1).toUpperCase()}
        String patternClasspathString = "org.olf.recurrence.recurrencePattern.RecurrencePattern${patternClassString.capitalize()}"

        // Create a new instance of the recurrence pattern class based on the previous parse
        final Class<? extends RecurrencePattern> rc = Class.forName(patternClasspathString)
        RecurrencePattern rp = rc.newInstance()
        JSONObject patternObj = new JSONObject(ru.pattern)
        patternObj.owner = recurrenceRule

        dataBinder.bind(rp, new SimpleMapDataBindingSource(patternObj))
        //Validate here
        recurrenceRule.pattern = rp
        recurrence.addToRules(recurrenceRule)
      }
    }else{
      // No recurrences bind the whole thing and validation will catch, hopefully
      dataBinder.bind(serialRuleset, new SimpleMapDataBindingSource(serialRulesetJson))
    }
    return serialRuleset
    
  }

  static Serial bindSerial (JSONObject serialJson) {
    // If object sent has reccurrence then validate as follows
    Serial serial = new Serial()
    if(serialJson?.serialRulesets?.size() > 0){
      Set serialObjKeys = new HashSet(serialJson.keySet())
      // Removing serial rulesets from serial object
      serialObjKeys.remove('serialRulesets')
      String[] serialKeyArray = serialObjKeys.toArray(new String[serialObjKeys.size()]);

      // Binding to new serial object without serial rulesets
      JSONObject serialObj = new JSONObject(serialJson, serialKeyArray)
      dataBinder.bind(serial, new SimpleMapDataBindingSource(serialObj))
      serialJson.serialRulesets.each { JSONObject serialRulesetJson ->
        SerialRuleset serialRuleset = bindSerialRuleset(serialRulesetJson)
        serial.addToSerialRulesets(serialRuleset)
      }
    } else {
      dataBinder.bind(serial, new SimpleMapDataBindingSource(serialJson))
    }
    return serial
 }

}
