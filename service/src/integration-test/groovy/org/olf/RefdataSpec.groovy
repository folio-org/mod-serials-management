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
class RefdataSpec extends BaseSpec {
  @Value('${local.server.port}')
  Integer serverPort

  void "Check sample data was loaded"() {
    log.debug("\n\nCheck sample data loaded\n\n");
    when: 'check sample data to complete'
      List list = doGet('/serials-management/refdata')
    then: 
      list.size() > 0
  }

  void "Check specific refdata category"() {
    log.debug("\n\nCheck sample data loaded\n\n");
    when: 'check specific refdata'
      List list = doGet('/serials-management/refdata/Serial/SerialStatus')
    then:
      list.size() > 0
  }

  void "Check specific refdata category with params"() {
    log.debug("\n\nCheck sample data loaded\n\n");
    when: 'check specific refdata'
      List resp = doGet('/serials-management/refdata/Serial/SerialStatus', [
        'offset': 0,
        'perPage': 10,
        'page': 0
      ])
    then:
      println("resp: ${resp}");
      resp.size() == 2
  }

  void "Check specific refdata category with params and filter and match"() {
    log.debug("\n\nCheck sample data loaded\n\n");
    when: 'check specific refdata'
      // This request is only there to game the coverage - we know these work. sigh.
      Map resp = doGet('/serials-management/refdata/Serial/SerialStatus', [
        'stats': true,
        'offset': 0,
        'perPage': 10,
        'page': 0,
        'filter': 'value==active',
        'match':'value',
        'term':'active'
      ])
    then:
      println("resp: ${resp}");
      resp.totalRecords == 1
  }
}

