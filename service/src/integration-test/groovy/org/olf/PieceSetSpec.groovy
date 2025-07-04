package org.olf

import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

@Slf4j
@Integration
@Stepwise
class PieceSetSpec extends BaseSpec {
  @Shared
  String serialId

  @Shared
  String startDate = "2024-01-01"

  @Shared
  Map serial_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/serial_data.json"))

  @Shared
  Map ruleset_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/ruleset_data.json"))

  private List getRecurrenceAndOmissionCombinations() {
    def combinations = []

    // Define the known outcomes for recurrence rules without any omissions
    def baseExpectedCounts = [
      day: 366,
      week: 53,
      monthDate: 12,
      monthWeekday: 12,
      yearDate: 1,
      yearWeekday: 1,
      yearMonthWeekday: 1
    ]

    def allRecurrences = ruleset_data.recurrence
    def allOmissions = ruleset_data.omission.rules

    allRecurrences.each { recurrenceName, recurrenceRule ->
      // Test the recurrence rule by itself (no omission rules)
      combinations.add([
        recurrenceName,
        recurrenceRule,
        'no_omission', // arbitrary name for the no omission case.
        null,            // No omission rules for this case
        baseExpectedCounts[recurrenceName]
      ])

      // test the recurrence rule combined with each type of omission rule
      allOmissions.eachWithIndex { omissionRule, index ->
        def omissionName = "omission_${omissionRule.patternType}_${index}"
        combinations.add([
          recurrenceName,
          recurrenceRule,
          omissionName,
          [omissionRule],
          null
        ])
      }
    }

    return combinations
  }

  void "Create an empty serial"() {

    when: "Post to create new empty serial named Empty serial Test"
    log.debug("Create new serial : Empty serial Test");
    Map respMap = doPost("/serials-management/serials", serial_data.activeSerial)
    serialId = respMap.id

    then: "Response is good and we have a new ID"
    serialId != null
  }

  @Unroll
  def "Generate pieces for recurrence '#recurrenceName' with #omissionName"() {
    given: "A request payload for generating pieces"
    def payload = [
      rulesetStatus: ruleset_data.rulesetStatus.active,
      recurrence: recurrenceRule,
      templateConfig: [
        templateString: "Test piece for #recurrenceName with #omissionName"
      ],
      owner: [
        id: serialId
      ],
      startDate: startDate
    ]

    // Conditionally add the omission rules to the payload
    if (omissionRules) {
      payload.omissions = [ rules: omissionRules ]
    }

    when: "We generate predicted pieces with the given rules"
    log.debug("Testing recurrence: ${recurrenceName} with omission: ${omissionName}")
    Map respMap = doPost("/serials-management/predictedPieces/generate", payload)

    then: "We get a response with pieces"
    respMap != null
    respMap.pieces instanceof List

    and: "The number of pieces matches the expected count"
    if (expectedPieceCount != null) {
      respMap.pieces.size() == expectedPieceCount
    }

    where:
    [recurrenceName, recurrenceRule, omissionName, omissionRules, expectedPieceCount] << getRecurrenceAndOmissionCombinations()
  }

// todo: How to test against labels?
  // todo: How to calculate expected omission sizes?

}
