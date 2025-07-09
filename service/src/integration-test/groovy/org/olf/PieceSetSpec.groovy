package org.olf

import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

import java.time.LocalDate;
import java.util.stream.Collectors;
import groovy.json.JsonOutput

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

  @Shared
  Map expectedOutcomes = new HashMap();

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
      allOmissions.each { omissionName, omissionRule ->
        combinations.add([
          recurrenceName,
          recurrenceRule,
          omissionName,   // The key from the map is the rule's name/id
          [omissionRule], // The value from the map is the rule object, wrapped in a list
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

  void "Load expected outcomes"() {
    when: "Expected values are loaded"
    def expectedValues = new File("src/integration-test/resources/expected_outcomes.json")
    expectedOutcomes = new groovy.json.JsonSlurper().parse(expectedValues)

    then:
    true
  }

  @Unroll
  def "Generate pieces for recurrence '#recurrenceName' with #omissionName"() {
    given: "A request payload for generating pieces"
    def payload = [
      rulesetStatus: ruleset_data.rulesetStatus.active,
      recurrence: recurrenceRule,
      templateConfig: [
        templateString: "{{chronology1.weekday}}"
      ],
      owner: [
        id: serialId
      ],
      startDate: startDate
    ]

    // Conditionally add the omission rules to the payload
    if (omissionRules) {
      payload.omission = [ rules: omissionRules ]
    }

    when: "We generate predicted pieces with the given rules"
    log.debug("Testing recurrence: ${recurrenceName} with omission: ${omissionName}")
    Map respMap = doPost("/serials-management/predictedPieces/generate", payload)

    then: "We get a response with pieces"
    respMap != null
    respMap.pieces instanceof List
    int n = 10;
    log.info("Dates: ")
    List<String> dates = respMap.pieces.stream()
      .map(p -> p.date) // get dates for each piece
      .sorted(Comparator.comparing(LocalDate::parse)) // sort
      .collect(Collectors.toList());

    if (respMap.pieces) {
      log.info(respMap.pieces.size().toString())
      List<String> omissionDates = respMap.pieces.stream()
        .filter(map -> "org.olf.internalPiece.InternalOmissionPiece".equals(map.get("class")))
        .map(map -> (String) map.get("date"))
        .sorted(Comparator.comparing(LocalDate::parse))
        .collect(Collectors.toList());

      List<String> publicationDates = respMap.pieces.stream()
        .filter(map -> "org.olf.internalPiece.InternalRecurrencePiece".equals(map.get("class")))
        .map(map -> (String) map.get("date"))
        .sorted(Comparator.comparing(LocalDate::parse))
        .collect(Collectors.toList());

      System.out.println("Dates of Omission Pieces (from Maps): " + omissionDates);
      log.info(dates.toListString())
      log.info(respMap.pieces.toString())

      assert expectedOutcomes.get(recurrenceName).get(omissionName).get("omissionDates") == omissionDates
      assert expectedOutcomes.get(recurrenceName).get(omissionName).get("publicationDates") == publicationDates

//      Map omissionOutput = [:]
//      if (expectedOutcomes.get(recurrenceName)) {
//        omissionOutput = expectedOutcomes.get(recurrenceName)
//      }
//      Map thisOmission = [:]
//      thisOmission.put("omissionDates", omissionDates)
//      thisOmission.put("publicationDates", publicationDates)
//      omissionOutput.put(omissionName, thisOmission)
//      expectedOutcomes.put(recurrenceName, omissionOutput)
//    }
    }

    and: "The number of pieces matches the expected count"
    if (expectedPieceCount != null) {
      respMap.pieces.size() == expectedPieceCount
    }

    where:
    [recurrenceName, recurrenceRule, omissionName, omissionRules, expectedPieceCount] << getRecurrenceAndOmissionCombinations()
  }
}
