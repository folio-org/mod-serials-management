package org.olf

import static groovyx.net.http.ContentTypes.*
import static groovyx.net.http.HttpBuilder.configure
import static org.springframework.http.HttpStatus.*

import com.k_int.okapi.OkapiHeaders
import com.k_int.okapi.OkapiTenantResolver
import geb.spock.GebSpec
import grails.gorm.multitenancy.Tenants
import grails.testing.mixin.integration.Integration
import groovy.json.JsonSlurper
import groovyx.net.http.ChainedHttpConfig
import groovyx.net.http.FromServer
import groovyx.net.http.HttpBuilder
import groovyx.net.http.HttpVerb
import java.time.LocalDate
import spock.lang.Stepwise
import spock.lang.Unroll
import spock.lang.Shared

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile
import org.springframework.beans.factory.annotation.Value;

import groovy.util.logging.Slf4j

@Slf4j
@Integration
@Stepwise
class PredictedPieceLifecycleSpecComplex extends BaseSpec {
  //This is a far more complex version of the integration tests I initially made
  @Value('${local.server.port}')
  Integer serverPort

  @Shared
  String serialId

  @Shared 
  String startDate = "2024-01-01"

  @Shared
  Map serial_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/serial_data.json"))

  @Shared
  Map ruleset_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/ruleset_data_complex.json"))

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

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       templateConfig:[
  //         templateString: "recurrence/day piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "The system responds with a list of 366 pieces"
  //     respList.size() == 366
  // }

  // void "Generate predicted pieces with a ruleset containing a 'week' recurrence rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.week,
  //       templateConfig:[
  //         templateString: "recurrence/week piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "week",
  //       startDate: startDate
  //     ])

  //   then: "The system responds with a list of 53 pieces"
  //     respList.size() == 53
  // }

  // void "Generate predicted pieces with a ruleset containing a 'month_date' recurrence rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.monthDate,
  //       templateConfig:[
  //         templateString: "recurrence/month_date piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "month_date",
  //       startDate: startDate
  //     ])

  //   then: "The system responds with a list of 12 pieces"
  //     respList.size() == 12
  // }

  // void "Generate predicted pieces with a ruleset containing a 'month_weekday' recurrence rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.monthWeekday,
  //       templateConfig:[
  //         templateString: "recurrence/month_weekday piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "month_weekday",
  //       startDate: startDate
  //     ])

  //   then: "The system responds with a list of 12 pieces"
  //     respList.size() == 12
  // }

  // void "Generate predicted pieces with a ruleset containing a 'year_date' recurrence rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.yearDate,
  //       templateConfig:[
  //         templateString: "recurrence/year_date piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "year_date",
  //       startDate: startDate
  //     ])

  //   then: "The system responds with a list of 1 piece"
  //     respList.size() == 1
  // }

  // void "Generate predicted pieces with a ruleset containing a 'year_weekday' recurrence rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.yearDate,
  //       templateConfig:[
  //         templateString: "recurrence/year_weekday piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "year_weekday",
  //       startDate: startDate
  //     ])

  //   then: "The system responds with a list of 1 piece"
  //     respList.size() == 1
  // }

  // void "Generate predicted pieces with a ruleset containing a 'year_month_weekday' recurrence rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.yearMonthWeekday,
  //       templateConfig:[
  //         templateString: "recurrence/year_month_weekday piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "year_month_weekday",
  //       startDate: startDate
  //     ])

  //   then: "The system responds with a list of 1 piece"
  //     respList.size() == 1
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and a 'day' omission rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       omission:[
  //         rules:[
  //           ruleset_data.omission.day
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "omission/day piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "Ensure that a single piece has been omitted"
  //     List omittedItems = respList.findAll(p -> p?.omissionOrigins)
  //     omittedItems.size() == 1
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and a 'week' omission rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       omission:[
  //         rules:[
  //           ruleset_data.omission.week
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "omission/week piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "Ensure that an entire week of pieces has been omitted"
  //     List omittedItems = respList.findAll(p -> p?.omissionOrigins)
  //     omittedItems.size() == 7
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and a range of 'week' omission rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       omission:[
  //         rules:[
  //           ruleset_data.omission.weekRange
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "omission/week_range piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "Ensure that 2 weeks of pieces has been omitted"
  //     List omittedItems = respList.findAll(p -> p?.omissionOrigins)
  //     omittedItems.size() == 14
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and a 'week_month' omission rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       omission:[
  //         rules:[
  //           ruleset_data.omission.weekMonth
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "omission/week_range piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])
    
  //   then: "Ensure that 1 week of pieces has been omitted"
  //     List omittedItems = respList.findAll(p -> p?.omissionOrigins)
  //     omittedItems.size() == 7
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and a 'month' omission rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       omission:[
  //         rules:[
  //           ruleset_data.omission.month
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "omission/month piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])
    
  //   then: "Ensure that 1 month of pieces has been omitted"
  //     List omittedItems = respList.findAll(p -> p?.omissionOrigins)
  //     omittedItems.size() == 31
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and a range of 'month' omission rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       omission:[
  //         rules:[
  //           ruleset_data.omission.monthRange
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "omission/month piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])
    
  //   then: "Ensure that 2 months of pieces has been omitted"
  //     List omittedItems = respList.findAll(p -> p?.omissionOrigins)
  //     omittedItems.size() == 60
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and an 'issue' omission rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       omission:[
  //         rules:[
  //           ruleset_data.omission.issue
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "omission/issue piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])
    
  //   then: "Ensure that issue '1' has been omitted"
  //     List omittedItems = respList.findAll(p -> p?.omissionOrigins)
  //     omittedItems.size() == 1
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and an 'issue_week' omission rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       omission:[
  //         rules:[
  //           ruleset_data.omission.issueWeek
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "omission/issue_week piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "Ensure that issue '1' within week '1' has been omitted"
  //     List omittedItems = respList.findAll(p -> p?.omissionOrigins)
  //     omittedItems.size() == 1
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and an 'issue_week_month' omission rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       omission:[
  //         rules:[
  //           ruleset_data.omission.issueWeekMonth
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "omission/issue_week_month piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "Ensure that issue '1' within week '1' within month '1' has been omitted"
  //     List omittedItems = respList.findAll(p -> p?.omissionOrigins)
  //     omittedItems.size() == 1
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and an 'issue_month' omission rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       omission:[
  //         rules:[
  //           ruleset_data.omission.issueMonth
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "omission/issue_month piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "Ensure that issue '1' within month '1' has been omitted"
  //     List omittedItems = respList.findAll(p -> p?.omissionOrigins)
  //     omittedItems.size() == 1
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and an 'issue' combination rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       combination:[
  //         rules:[
  //           ruleset_data.combination.issue
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "combination/issue piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "Ensure that the first issue has been combined with the second"
  //     List combinedItems = respList.findAll(p -> p?.combinationOrigins)
  //     combinedItems[0].recurrencePieces.size() == 2
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and an 'issue_week' combination rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       combination:[
  //         rules:[
  //           ruleset_data.combination.issueWeek
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "combination/issue_week piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "Ensure that the first issue in week '1' has been combined with the second"
  //     List combinedItems = respList.findAll(p -> p?.combinationOrigins)
  //     combinedItems[0].recurrencePieces.size() == 2
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and an 'issue_month' combination rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       combination:[
  //         rules:[
  //           ruleset_data.combination.issueMonth
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "combination/issue_month piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "Ensure that the first issue in month '1' has been combined with the second"
  //     List combinedItems = respList.findAll(p -> p?.combinationOrigins)
  //     combinedItems[0].recurrencePieces.size() == 2
  // }

  // void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and an 'issue_week_month' combination rule"() {
  //   when: "We ask the system to generate predicted pieces"
  //     List respList = doPost("/serials-management/predictedPieces/generate", [
  //       rulesetStatus: ruleset_data.rulesetStatus.active,
  //       recurrence: ruleset_data.recurrence.day,
  //       combination:[
  //         rules:[
  //           ruleset_data.combination.issueWeekMonth
  //         ]
  //       ],
  //       templateConfig:[
  //         templateString: "combination/issue_week_month piece {{standardTM.index}}"
  //       ],
  //       owner:[
  //         id: serialId
  //       ],
  //       patternType: "day",
  //       startDate: startDate
  //     ])

  //   then: "Ensure that the first issue in week '1' of month '1' has been combined with the second"
  //     List combinedItems = respList.findAll(p -> p?.combinationOrigins)
  //     combinedItems[0].recurrencePieces.size() == 2
  // }

  // // void "Generate predicted pieces with a ruleset containing a 'year' recurrence rule and all template config rules"() {
  // //   when: "We ask the system to generate predicted pieces"
  // //     List respList = doPost("/serials-management/predictedPieces/generate", [
  // //       rulesetStatus: ruleset_data.rulesetStatus.active,
  // //       recurrence: ruleset_data.recurrence.year,
  // //       templateConfig:[
  // //         rules: [
  // //           ruleset_data.templateConfig.chronologyDate,
  // //           ruleset_data.templateConfig.chronologyMonth,
  // //           ruleset_data.templateConfig.chronologyYear,
  // //           ruleset_data.templateConfig.enumerationNumeric,
  // //           ruleset_data.templateConfig.enumerationTextual,
  // //         ],
  // //         templateString: "combination/issue_week_month piece {{standardTM.index}}"
  // //       ],
  // //       owner:[
  // //         id: serialId
  // //       ],
  // //       patternType: "year",
  // //       startDate: startDate
  // //     ])

  // //   then: "Ensure that the first issue in week '1' of month '1' has been combined with the second"
  // //     respList.size() == 1
  // // }
}

