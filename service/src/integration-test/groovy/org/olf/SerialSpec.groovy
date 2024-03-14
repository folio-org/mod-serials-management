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
class SerialSpec extends BaseSpec {
  @Value('${local.server.port}')
  Integer serverPort

  @Shared
  String serialId

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

  void "Create a ruleset of a daily recurrence"() {
    when:"We ask the system to display the previously created serial"
      def resp = doGet("/serials-management/serials/${serialId}")

    then: "The system responds with a serial with a ruleset list of 0"
      resp.serialRulesets.size() == 0

    when: "Post to create new ruleset with daily recurrence"
      log.debug("Create new rulset");

      def respMap = doPost("/serials-management/rulesets", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.day,
        templateConfig:[
          templateString: "123"
        ],
        owner:[
          id: serialId
        ],
        patternType: "day"
      ]);
    
    then: "Response is good and we have a new ID"
      respMap.id != null
  }
  
  void "Create a Serial with status closed with description Test Description" () {

    log.debug("Create Serial Tests....");
    
    when: "Query for Serial with status closed"
    
      List resp = doGet("/serials-management/serials", [
        filters:[
          "serialStatus.value=closed" // Case insensitive match
        ]
      ])
      
    then: "No serial found"
      resp.size() == 0
      

    when: "Post to create new serial with closed status and Test Description"
      log.debug("Create new serial");
      Map respMap = doPost("/serials-management/serials", {
        'serialStatus' 'Closed'
        'description' 'Test Description'
      })
    
    then: "Response is good and we have a new ID"
      respMap.id != null

    log.debug("After post....");
      
    when: "Query for Serial with status closed"
    
      serialId = respMap.id
    
      resp = doGet("/serials-management/serials", [
        filters:[
          "serialStatus.value=closed" 
        ]
      ])
      
    then: "Serial found and ID matches returned one from before"
      resp.size() == 1
      log.debug("Looked up serial ${resp[0]}");
      resp[0].id == serialId

    when:"We do a get for that serial"
      def result_of_direct_get = doGet("/serials-management/serials/${serialId}");

    then:"Dump the result of the get"
      log.debug("Result of GET: ${result_of_direct_get}");
  }

}

