package org.olf

import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

import java.time.LocalDate;
import java.util.stream.Collectors;

@Slf4j
@Integration
@Stepwise
class PieceSetSpec extends BaseSpec {
  /*
  These tests use a combinatorial approach using Spock's "where" block to test many different
  recurrence rules against combination OR omission rules. The tests use rule data from "ruleset_data",
  so are heavily dependent on this file. Any changes to data in ruleset_data are likely to lead to changes
  in the expected values of these tests or require a change in the test logic.

  The class begins by creating a new serial which then has rulesets attached to it so piece sets can be
  generated.

  Expected values are loaded in from "/resource/expected_outcomes.json" which contains lists of expected
  dates (publication dates + omission dates/combined dates) in the form : {omission/combination -> recurrence_name -> rule_name -> {publicationDates + omission/combinationDates} }

   Combinations are generated using two private helper functions which are called in the "where: " block.

   The actual integration test is quite simple. It creates a payload with the relevant omission/combination rules
   then hits the /generate endpoint to generate a piece set. The dates are then extracted from the piece set
   for each type of piece (recurrence, omission, combination) and these different dates are asserted against expected values.
   */

  @Shared
  String serialId

  @Shared
  String startDate = "2024-01-01"

  @Shared
  Map serial_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/serial_data.json"))

  @Shared
  Map ruleset_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/ruleset_data.json"))

  @Shared
  Map expectedOutcomesOmissionRules = new HashMap();

  @Shared
  Map expectedOutcomesCombinationRules = new HashMap();

  // Helper function to create all the combinations of reccurence and omission rules in ruleset_data.json
  private List getRecurrenceOmissionScenarios() {
    def combinations = []

    def allRecurrences = ruleset_data.recurrence
    def allOmissions = ruleset_data.omission.rules

    allRecurrences.each { recurrenceName, recurrenceRule ->
      // Test the recurrence rule by itself (no omission or combination rules)
      combinations.add([
        recurrenceName,
        recurrenceRule,
        'no_rules', // generic name for the base case
        null, // No rule name
        null, // No omission rules
        null, // No combination rules
      ])

      // Test the recurrence rule combined with each type of omission rule
      allOmissions.each { omissionName, omissionRule ->
        combinations.add([
          recurrenceName,
          recurrenceRule,
          "omission: ${omissionName}",
          [omissionRule], // Omission rules
          null           // No combination rules
        ])
      }
    }

    return combinations
  }

  // Helper function to create all the combinations of reccurence and combination rules in ruleset_data.json
  private List getRecurrenceCombinationScenarios() {
    def combinations = []
    def allRecurrences = ruleset_data.recurrence
    def allCombinations = ruleset_data.combination.rules

    allRecurrences.each { recurrenceName, recurrenceRule ->
      allCombinations.each { combinationName, combinationRule ->
        combinations.add([
          recurrenceName,
          recurrenceRule,
          "combination: ${combinationName}",
          null, // No omission rules
          [combinationRule] // Combination rules
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
    expectedOutcomesOmissionRules = new groovy.json.JsonSlurper().parse(expectedValues).omission
    expectedOutcomesCombinationRules = new groovy.json.JsonSlurper().parse(expectedValues).combination

    then:
    true
  }

  @Unroll
  def "Generate pieces for recurrence '#recurrenceName' with #scenarioDescription"() {
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

    // Conditionally add the omission or combination rules to the payload
    if (omissionRules) {
      payload.omission = [ rules: omissionRules ]
    }
    if (combinationRules) {
      payload.combination = [ rules: combinationRules ]
    }

    when: "We generate predicted pieces with the given rules"
    log.debug("Testing scenario: Recurrence '${recurrenceName}' with ${scenarioDescription}")
    Map respMap = doPost("/serials-management/predictedPieces/generate", payload)

    String ruleName = scenarioDescription.split(":").size() > 1 ? scenarioDescription.split(":")[1].trim() : "no_rules"

    then: "We get a response with pieces and the output matches expectations"
    respMap != null
    respMap.pieces instanceof List

    if (respMap.pieces) {
      log.info("Total pieces generated: ${respMap.pieces.size()}")

      // Get lists of omission dates, publication dates and combined dates.
      List<String> omissionDates = respMap.pieces.stream()
        .filter(p -> "org.olf.internalPiece.InternalOmissionPiece".equals(p.get("class")))
        .map(p -> (String) p.get("date"))
        .sorted(Comparator.comparing(LocalDate::parse))
        .collect(Collectors.toList());

      List<String> publicationDates = respMap.pieces.stream()
        .filter(p -> "org.olf.internalPiece.InternalRecurrencePiece".equals(p.get("class")))
        .map(p -> (String) p.get("date"))
        .sorted(Comparator.comparing(LocalDate::parse))
        .collect(Collectors.toList());

      List<String> combinedDates = respMap.pieces.stream()
        .filter(p -> "org.olf.internalPiece.InternalCombinationPiece".equals(p.get("class")))
        .flatMap(p -> ((List<Map>) p.get("recurrencePieces")).stream()) // get the list of dates from the combined recurrence pieces
        .map(rp -> (String) rp.get("date")) // From the flat stream, get the list of dates.
        .sorted(Comparator.comparing(LocalDate::parse))
        .collect(Collectors.toList());

      log.info("Omission dates: ${omissionDates}")
      log.info("Publication dates: ${publicationDates}")
      log.info("Combined dates: ${combinedDates}")

      // Load the expected values
      Map expectedRuleOutcome = expectedOutcomesOmissionRules.get(recurrenceName)?.get(ruleName)

      // If it's a combination rule test, then assert against the combinedDates and publicationDates
      if (scenarioDescription.startsWith("combination:")) {
        expectedRuleOutcome = expectedOutcomesCombinationRules.get(recurrenceName)?.get(ruleName) //Have stored the expected values for combination rules in a separate json for now.
        assert expectedRuleOutcome.get("publicationDates") == publicationDates
        assert expectedRuleOutcome.get("combinedDates") == combinedDates
      } else {
        // Else assert that the omissionDates and publicationDates match those we expect.
        assert expectedRuleOutcome.get("omissionDates") == omissionDates
        assert expectedRuleOutcome.get("publicationDates") == publicationDates
      }
    }

    where:
    [recurrenceName, recurrenceRule, scenarioDescription, omissionRules, combinationRules] <<
      (getRecurrenceOmissionScenarios() + getRecurrenceCombinationScenarios())
  }
}
