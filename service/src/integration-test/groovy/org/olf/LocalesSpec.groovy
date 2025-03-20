package org.olf

import grails.testing.mixin.integration.Integration

import spock.lang.Stepwise

import groovy.util.logging.Slf4j

@Slf4j
@Integration
@Stepwise
class LocalesSpec extends BaseSpec {

  void "List locales without keyLocales query param"() {

    when:"We ask the system to fetch locales with the keyLocales query param set to false"
      List localesList = doGet("/serials-management/locales?keyLocales=false")

    then: "The system responds with a list of all locales with correct shape"
      localesList.size() == 1017

      localesList[1].label == "Afrikaans"
      localesList[1].value == "af"
  }

  void "List locales with keyLocales query param"() {

    when:"We ask the system to fetch locales with the keyLocales query param set to true"
      List localesList = doGet("/serials-management/locales?keyLocales=true")

    then: "The system responds with a list of key locales with correct shape"
      localesList.size() == 213

      localesList[1].label == "Afrikaans"
      localesList[1].value == "af"
  }
}

