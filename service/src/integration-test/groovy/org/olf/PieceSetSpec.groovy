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

      log.debug("Omission dates: ${omissionDates}")
      log.debug("Publication dates: ${publicationDates}")
      log.debug("Combined dates: ${combinedDates}")

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

  void "CREATE a predicted pieces using a ruleset and a combination rule"() {
    when: "We ask the system to create predicted pieces"
    Map combinationRule = [:]
    combinationRule.put("rules", [ruleset_data.combination.rules.combine_by_issue_2])

    Map rulesetResponse = doPost("/serials-management/rulesets", [
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

    Map respMap = doPost("/serials-management/predictedPieces/create", [
      id: rulesetResponse.id,
      startDate: startDate
    ])

    then: "The system responds with a list of 365 pieces"
    respMap?.pieces.size() == 365
    List combinedItems = respMap?.pieces.findAll(p -> p?.combinationOrigins)
    combinedItems.size() == 1
  }

  void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and multiple 'issue' combination rules"() {
    Map combinationRule = [:]
    combinationRule.put("rules", [ruleset_data.combination.rules.combine_by_issue_2, ruleset_data.combination.rules.combine_by_issue_10])
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
    combinedItems.size() == 2
    List totalPiecesCombined = []
    respMap?.pieces.forEach{p -> {
      if (p?.recurrencePieces) p.recurrencePieces.forEach{rp -> totalPiecesCombined.add(rp)}
    }}
    totalPiecesCombined.size() == 4
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

  void "CREATE a predicted pieces with a ruleset and an omission rule"() {
    when: "We ask the system to create predicted pieces"
    Map omissionRule = [:]
    omissionRule.put("rules", [ruleset_data.omission.rules.omit_by_month_range_may_to_june])
    Map rulesetResponse = doPost("/serials-management/rulesets",  [
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

    Map respMap = doPost("/serials-management/predictedPieces/create", [
      id: rulesetResponse.id,
      startDate: startDate
    ])

    then: "The system responds with a list of 61 omitted pieces"
    List omittedItems = respMap?.pieces.findAll(p -> p?.omissionOrigins)
    omittedItems.size() == 61
  }

  void "CREATE a predicted pieces with the example ruleset"() {
    when: "We ask the system to create predicted pieces"
    Map rulesetResponse = doPost("/serials-management/rulesets", [
      rulesetStatus: ruleset_data.rulesetStatus.active,
      recurrence: ruleset_data.recurrence.day,
      templateConfig:[
        templateString: "omission/day piece"
      ],
      owner:[
        id: serialId
      ],
      patternType: "day",
      startDate: startDate
    ])

    Map respMap = doPost("/serials-management/predictedPieces/create", [id: rulesetResponse.id, startDate: startDate])

    then: "The system responds with a list of 366 pieces"
    respMap?.pieces.size() == 366
  }

  void "Generate predicted pieces with a ruleset containing a 'day' recurrence rule and multiple omission rules"() {
    when: "We ask the system to generate predicted pieces"
    Map omissionRule = [:]
    omissionRule.put("rules", [ruleset_data.omission.rules.omit_by_month_range_may_to_june, ruleset_data.omission.rules.omit_by_first_wednesday_in_january, ruleset_data.omission.rules.omit_by_issue_week_month])
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
    omittedItems.size() == 63
    def actualDates = omittedItems.collect { it.date.toString() }.sort()
    def expectedDates = ["2024-01-03", "2024-05-01", "2024-05-02", "2024-05-03", "2024-05-04", "2024-05-05", "2024-05-06", "2024-05-07", "2024-05-08", "2024-05-09", "2024-05-10", "2024-05-11", "2024-05-12", "2024-05-13", "2024-05-14", "2024-05-15", "2024-05-16", "2024-05-17", "2024-05-18", "2024-05-19", "2024-05-20", "2024-05-21", "2024-05-22", "2024-05-23", "2024-05-24", "2024-05-25", "2024-05-26", "2024-05-27", "2024-05-28", "2024-05-29", "2024-05-30", "2024-05-31", "2024-06-01", "2024-06-02", "2024-06-03", "2024-06-04", "2024-06-05", "2024-06-06", "2024-06-07", "2024-06-08", "2024-06-09", "2024-06-10", "2024-06-11", "2024-06-12", "2024-06-13", "2024-06-14", "2024-06-15", "2024-06-16", "2024-06-17", "2024-06-18", "2024-06-19", "2024-06-20", "2024-06-21", "2024-06-22", "2024-06-23", "2024-06-24", "2024-06-25", "2024-06-26", "2024-06-27", "2024-06-28", "2024-06-29", "2024-06-30", "2024-11-01"]
    assert actualDates == expectedDates
  }

  void "Generate predicted pieces with a ruleset containing a day recurrence and multiple OVERLAPPING combination rules"() {
    Map combinationRule = [:]
    combinationRule.put("rules", [ruleset_data.combination.rules.combine_by_issue_1, ruleset_data.combination.rules.combine_by_issue_2])
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
    List totalPiecesCombined = []
    respMap?.pieces.forEach{p -> {
      if (p?.recurrencePieces) p.recurrencePieces.forEach{rp -> totalPiecesCombined.add(rp)}
    }}
    totalPiecesCombined.size() == 3
    def actualDates = combinedItems.collectMany { combinedPiece ->
      combinedPiece?.recurrencePieces?.collect { recurrencePiece ->
        recurrencePiece?.date?.toString()
      }
    }
    def expectedDates = ["2024-01-01", "2024-01-02", "2024-01-03"]
    actualDates == expectedDates

  }

  void "Generate predicted pieces with a ruleset containing a day recurrence rule and multiple OVERLAPPING omission rules"() {
    when: "We ask the system to generate predicted pieces"
    Map omissionRule = [:]
    omissionRule.put("rules", [ruleset_data.omission.rules.omit_by_day_of_month_2nd, ruleset_data.omission.rules.omit_by_month_april])
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
    omittedItems.size() == 41
    def actualDates = omittedItems.collect { it.date.toString() }.sort()
    def expectedDates = ["2024-01-02", "2024-02-02", "2024-03-02", "2024-04-01", "2024-04-02", "2024-04-03", "2024-04-04", "2024-04-05", "2024-04-06", "2024-04-07", "2024-04-08", "2024-04-09", "2024-04-10", "2024-04-11", "2024-04-12", "2024-04-13", "2024-04-14", "2024-04-15", "2024-04-16", "2024-04-17", "2024-04-18", "2024-04-19", "2024-04-20", "2024-04-21", "2024-04-22", "2024-04-23", "2024-04-24", "2024-04-25", "2024-04-26", "2024-04-27", "2024-04-28", "2024-04-29", "2024-04-30", "2024-05-02", "2024-06-02", "2024-07-02", "2024-08-02", "2024-09-02", "2024-10-02", "2024-11-02", "2024-12-02"]
    assert actualDates == expectedDates
  }
}
