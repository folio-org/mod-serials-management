package org.olf

import grails.testing.mixin.integration.Integration
import spock.lang.Stepwise

import groovy.util.logging.Slf4j

@Slf4j
@Integration
@Stepwise
class SerialSpec extends BaseSpec {

  void "List Initial Serials"() {
    when:'We ask the system to list known Serials'
      List resp = doGet('/serials-management/serials')

    then: 'The system responds with a list of 0'
      resp.size() == 0
  }

  void "Check creating a serial and then query for created serial"(serialStatus, description) {
    when: "Post to create new empty serial with description ${description}"
      Map serialPostMap = doPost('/serials-management/serials', [
        serialStatus: serialStatus,
        description: description
      ])

    then: 'Response is good and we have a new serial'
      serialPostMap.id != null
      serialPostMap.description == description
      serialPostMap.serialStatus.value == serialStatus

    when: 'Query for the created serial'
      String serialId = serialPostMap.id
      Map serialGetMap = doGet("/serials-management/serials/${serialId}")

    then: 'Serial found and ID matches returned one from before'
      serialGetMap.id == serialId
      serialGetMap.description == description
      serialGetMap.serialStatus.value == serialStatus

    // Using where is a Spock feature that allows us to run the same test with different parameters
    // Excessive for this test but can be used as an example
    where:
      serialStatus << ['active', 'closed']
      description << ['Active serial test', 'Closed serial test']
  }

  void "Check creating a serial and then updating the created serial"() {
    when: 'Post to create new empty serial with description Pre-update serial test'
      Map serialPostMap = doPost('/serials-management/serials', [
        serialStatus: 'active',
        description: 'Pre-update serial test'
      ])

    then: 'Response is good and we have a new serial'
      serialPostMap.id != null
      serialPostMap.description == 'Pre-update serial test'
      serialPostMap.serialStatus.value == 'active'

    when: 'Query for the created serial'
      String serialId = serialPostMap.id
      Map serialGetMap = doGet("/serials-management/serials/${serialId}")

    then: 'Serial found and ID matches returned one from before'
      serialGetMap.id == serialId
      serialGetMap.description == 'Pre-update serial test'
      serialGetMap.serialStatus.value == 'active'

    when: 'Update previously created serial'
      Map serialPutMap = doPut("/serials-management/serials/${serialId}", [
        description: 'Post-update serial test'
      ])
    
    then: 'Serial found and ID matches returned one from before, as well as updated description'
      serialPutMap.id == serialId
      serialPutMap.description == 'Post-update serial test'

    when: 'Query for the updated serial'
      serialGetMap = doGet("/serials-management/serials/${serialId}")

    then: 'Serial found and ID matches returned one from before, aswell as updated description'
      serialGetMap.id == serialId
      serialGetMap.description == 'Post-update serial test'
  }

  void "List Current Serials"() {
    when:'We ask the system to list known Serials'
      List resp = doGet('/serials-management/serials')

    then: 'The system responds with a list of 3, matching all the serials we created'
      resp.size() == 3
  }

}
