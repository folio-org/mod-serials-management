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
class PredictedPieceSpec extends BaseSpec {
  @Value('${local.server.port}')
  Integer serverPort

  @Shared
  String serialId

  @Shared 
  String startDate = "2024-01-01"

  @Shared
  Map serial_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/serial_data.json"))

  @Shared
  Map ruleset_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/ruleset_data.json"))

  void "List Current PredictedPieceSets"() {

    when:"We ask the system to list known PredictedPieceSets"
      List resp = doGet("/serials-management/predictedPieces")

    then: "The system responds with a list of 0"
      resp.size() == 0
  }

  void "Create an empty serial"() {

    when: "Post to create new empty serial named Empty serial Test"
      log.debug("Create new serial : Empty serial Test");
      Map respMap = doPost("/serials-management/serials", serial_data.activeSerial)
      serialId = respMap.id

    then: "Response is good and we have a new ID"
      serialId != null
  }

  void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule"() {
    when: "We ask the system to generate predicted pieces"
      Map respMap = doPost("/serials-management/predictedPieces/generate", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.day,
        templateConfig:[
          templateString: "recurrence/day piece {{standardTM.index}}"
        ],
        owner:[
          id: serialId
        ],
        patternType: "day",
        startDate: startDate
      ])

    then: "The system responds with a list of 366 pieces"
      respMap?.pieces.size() == 366
  }

  void "Generate predicted pieces with a ruleset containing a 'week' recurrence rule"() {
    when: "We ask the system to generate predicted pieces"
      Map respMap = doPost("/serials-management/predictedPieces/generate", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.week,
        templateConfig:[
          templateString: "recurrence/week piece {{standardTM.index}}"
        ],
        owner:[
          id: serialId
        ],
        patternType: "week",
        startDate: startDate
      ])

    then: "The system responds with a list of 53 pieces"
      respMap?.pieces.size() == 53
  }

  void "Generate predicted pieces with a ruleset containing a 'month_date' recurrence rule"() {
    when: "We ask the system to generate predicted pieces"
      Map respMap = doPost("/serials-management/predictedPieces/generate", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.monthDate,
        templateConfig:[
          templateString: "recurrence/month_date piece {{standardTM.index}}"
        ],
        owner:[
          id: serialId
        ],
        patternType: "month_date",
        startDate: startDate
      ])

    then: "The system responds with a list of 12 pieces"
      respMap?.pieces.size() == 12
  }

  void "Generate predicted pieces with a ruleset containing a 'month_weekday' recurrence rule"() {
    when: "We ask the system to generate predicted pieces"
      Map respMap = doPost("/serials-management/predictedPieces/generate", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.monthWeekday,
        templateConfig:[
          templateString: "recurrence/month_weekday piece {{standardTM.index}}"
        ],
        owner:[
          id: serialId
        ],
        patternType: "month_weekday",
        startDate: startDate
      ])

    then: "The system responds with a list of 12 pieces"
      respMap?.pieces.size() == 12
  }

  void "Generate predicted pieces with a ruleset containing a 'year_date' recurrence rule"() {
    when: "We ask the system to generate predicted pieces"
      Map respMap = doPost("/serials-management/predictedPieces/generate", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.yearDate,
        templateConfig:[
          templateString: "recurrence/year_date piece {{standardTM.index}}"
        ],
        owner:[
          id: serialId
        ],
        patternType: "year_date",
        startDate: startDate
      ])

    then: "The system responds with a list of 1 piece"
      respMap?.pieces.size() == 1
  }

  void "Generate predicted pieces with a ruleset containing a 'year_weekday' recurrence rule"() {
    when: "We ask the system to generate predicted pieces"
      Map respMap = doPost("/serials-management/predictedPieces/generate", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.yearDate,
        templateConfig:[
          templateString: "recurrence/year_weekday piece {{standardTM.index}}"
        ],
        owner:[
          id: serialId
        ],
        patternType: "year_weekday",
        startDate: startDate
      ])

    then: "The system responds with a list of 1 piece"
      respMap?.pieces.size() == 1
  }

  void "Generate predicted pieces with a ruleset containing a 'year_month_weekday' recurrence rule"() {
    when: "We ask the system to generate predicted pieces"
      Map respMap = doPost("/serials-management/predictedPieces/generate", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.yearMonthWeekday,
        templateConfig:[
          templateString: "recurrence/year_month_weekday piece {{standardTM.index}}"
        ],
        owner:[
          id: serialId
        ],
        patternType: "year_month_weekday",
        startDate: startDate
      ])

    then: "The system responds with a list of 1 piece"
      respMap?.pieces.size() == 1
  }

  void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and all possible omission rules"() {
    when: "We ask the system to generate predicted pieces"
    Map omissionRule = [:]
    omissionRule.put("rules", [ruleset_data.omission.rules.omit_by_month_range_may_to_june])
      Map respMap = doPost("/serials-management/predictedPieces/generate", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.day,
        omission: omissionRule,
        templateConfig:[
          templateString: "omission/day piece"
        ],
        owner:[
          id: serialId
        ],
        patternType: "day",
        startDate: startDate
      ])

    then: "Ensure that all omitted pieces exist"
      List omittedItems = respMap?.pieces.findAll(p -> p?.omissionOrigins)
      omittedItems.size() == 61
  }

  void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and an 'issue' combination rule"() {
    Map combinationRule = [:]
    combinationRule.put("rules", [ruleset_data.combination.rules.combine_by_issue_2])
    when: "We ask the system to generate predicted pieces"
      Map respMap = doPost("/serials-management/predictedPieces/generate", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.day,
        combination: combinationRule,
        templateConfig:[
          templateString: "combination/issue piece"
        ],
        owner:[
          id: serialId
        ],
        patternType: "day",
        startDate: startDate
      ])

    then: "Ensure all combined issues exist"
      List combinedItems = respMap?.pieces.findAll(p -> p?.combinationOrigins)
      combinedItems.size() == 1
  }

  void "Generate predicted pieces with a ruleset containing a 'year' recurrence rule and all template config rules"() {
    when: "We ask the system to generate predicted pieces"
      Map respMap = doPost("/serials-management/predictedPieces/generate", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.yearDate,
        templateConfig: ruleset_data.templateConfigurations.templateConfig,
        owner:[
          id: serialId
        ],
        patternType: "year_date",
        startDate: startDate
      ])

    then: "Ensure that the first issue in week '1' of month '1' has been combined with the second"
      respMap?.pieces.size() == 1
  }

  void "Create predicted pieces"() {
    when: "We ask the system to create a ruleset"
      Map respMap = doPost("/serials-management/rulesets", [
        rulesetStatus: ruleset_data.rulesetStatus.active,
        recurrence: ruleset_data.recurrence.yearDate,
        templateConfig: [ templateString: "123"],
        owner:[
          id: serialId
        ],
        patternType: "year_date",
        startDate: startDate
      ])

    then: "Ensure that response is ok and we have a new ruleset"
      respMap.id != null

     when: "We ask the system to create a predicted piece set based off the previous ruleset"
       respMap = doPost("/serials-management/predictedPieces/create", [
         id: respMap.id,
         startDate: startDate
       ])

     then: "Ensure that response is ok and we have a new predicted piece set"
       respMap.id != null
  }
}

