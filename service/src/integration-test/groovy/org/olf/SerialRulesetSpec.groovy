package org.olf

import grails.testing.mixin.integration.Integration
import groovy.json.JsonSlurper
import spock.lang.Stepwise
import spock.lang.Shared

import org.springframework.beans.factory.annotation.Value;

import groovy.util.logging.Slf4j

@Slf4j
@Integration
@Stepwise
class SerialRulesetSpec extends BaseSpec {
  @Value('${local.server.port}')
  Integer serverPort

  @Shared
  String serialId

  @Shared
  String rulesetId

  @Shared
  Map ruleset_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/ruleset_data.json"))


  void "List Current Serials"() {

    when:"We ask the system to list known Serials"
      List resp = doGet("/serials-management/serials")

    then: "The system responds with a list of 0"
      resp.size() == 0
  }

  void "Check creating an empty serial"() {

    when: "Post to create new empty serial named Empty serial Test"
      log.debug("Create new serial : Empty serial Test");
      Map respMap = doPost("/serials-management/serials", {
        'serialStatus' 'Active' // This can be the value or id but not the label
      })
      serialId = respMap.id

    then: "Response is good and we have a new ID"
      serialId != null
  }

  void "Create a ruleset of an active status, then update the status to deprecated and draft and back to active"() {
    when:"We ask the system to display the previously created serial"
      def resp = doGet("/serials-management/serials/${serialId}")

    then: "The system responds with a serial with a ruleset list of 0"
      resp.serialRulesets.size() == 0

    when: "Post to create new ruleset with daily recurrence and active status"
      log.debug("Create new rulset");

      def respMap = doPost("/serials-management/rulesets", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.day,
        templateConfig:[
          templateString: "ruleset spec"
        ],
        owner:[
          id: serialId
        ],
        patternType: "day"
      ]);
    
    then: "Response is good and we have a new ID and a status of active"
      respMap.id != null
      def rulesetId = respMap.id
      respMap.rulesetStatus.value == 'active'
    
    when: "Post to update serial ruleset status to deprecated"
      log.debug("Update ruleset status to deprecated");

      def deprecatedRuleset = doPost("/serials-management/rulesets/${rulesetId}/deprecated", respMap);

    then: "Response is good and a status of deprecated"
      deprecatedRuleset.rulesetStatus.value == 'deprecated'

    when: "Post to update serial ruleset status to draft"
      log.debug("Update ruleset status to draft");

      def draftRuleset = doPost("/serials-management/rulesets/${rulesetId}/draft", respMap);

    then: "Response is good and a status of deprecated"
      draftRuleset.rulesetStatus.value == 'draft'
    
    when: "Post to update serial ruleset status to active"
      log.debug("Update ruleset status to active");

      def activeRuleset = doPost("/serials-management/rulesets/${rulesetId}/active", respMap);

    then: "Response is good and a status of active"
      activeRuleset.rulesetStatus.value == 'active'
  }

  void "Create a serial with an active ruleset and then attempt to add another active rulerset"() {
    when: "Post to create new empty serial named Empty serial Test"
      log.debug("Create new serial : Empty serial Test");
      Map respMap = doPost("/serials-management/serials", {
        'serialStatus' 'Active' // This can be the value or id but not the label
      })
      serialId = respMap.id

    then: "Response is good and we have a new ID"
      serialId != null

    when: "Post to create new ruleset with daily recurrence and active status"
      log.debug("Create new rulset");

      respMap = doPost("/serials-management/rulesets", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.day,
        templateConfig:[
          templateString: "ruleset spec"
        ],
        owner:[
          id: serialId
        ],
        patternType: "day"
      ]);
    
    then: "Response is good and we have a new ID and a status of active"
      respMap.id != null
      def activeRulesetId = respMap.id
      respMap.rulesetStatus.value == 'active'

    when: "Post another ruleset with daily recurrence and active status"
      log.debug("Create new rulset");

      respMap = doPost("/serials-management/rulesets", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.day,
        templateConfig:[
          templateString: "ruleset spec"
        ],
        owner:[
          id: serialId
        ],
        patternType: "day"
      ]);
    
    then: "Response is good and we have a new ID and a status of active"
      respMap.id != null
      def rulesetId = respMap.id
      respMap.rulesetStatus.value == 'active'

    when:"We ask the system to check that the pervious ruleset has been deprecated"
      respMap = doGet("/serials-management/rulesets/${activeRulesetId}")

    then: "The system responds with a ruleset of status deprecated"
      respMap.rulesetStatus.value == 'deprecated'
}

  void "Create a serial with an active ruleset and then attempt to activate a previously deprecated ruleset"() {
    when: "Post to create new empty serial named Empty serial Test"
      log.debug("Create new serial : Empty serial Test");
      Map respMap = doPost("/serials-management/serials", {
        'serialStatus' 'Active' // This can be the value or id but not the label
      })
      serialId = respMap.id

    then: "Response is good and we have a new ID"
      serialId != null

    when: "Post to create new ruleset with daily recurrence and active status"
      log.debug("Create new rulset");

      respMap = doPost("/serials-management/rulesets", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.day,
        templateConfig:[
          templateString: "ruleset spec"
        ],
        owner:[
          id: serialId
        ],
        patternType: "day"
      ]);
    
    then: "Response is good and we have a new ID and a status of active"
      respMap.id != null
      def activeRulesetId = respMap.id
      respMap.rulesetStatus.value == 'active'

    when: "Post another ruleset with daily recurrence and active status"
      log.debug("Create new rulset");

      respMap = doPost("/serials-management/rulesets", [
        rulesetStatus: ruleset_data.rulesetStatus.deprecated,
        recurrence: ruleset_data.recurrence.day,
        templateConfig:[
          templateString: "ruleset spec"
        ],
        owner:[
          id: serialId
        ],
        patternType: "day"
      ]);
    
    then: "Response is good and we have a new ID and a status of deprecated"
      respMap.id != null
      def deprecatedRulesetId = respMap.id
      respMap.rulesetStatus.value == 'deprecated'

    when:"We ask the system to acitvate the previously deprecated ruleset"
      respMap = doPost("/serials-management/rulesets/${deprecatedRulesetId}/active", respMap)

    then: "The system responds with a ruleset of status active"
      respMap.rulesetStatus.value == 'active'

    when:"We ask the system to check that the previously active ruleset has been deprecated"
      respMap = doGet("/serials-management/rulesets/${activeRulesetId}")

    then: "The system responds with a ruleset of status deprecated"
      respMap.rulesetStatus.value == 'deprecated'
}
}

