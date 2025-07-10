package org.olf

import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import org.olf.templateConfig.TemplateConfig
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDate
import java.util.stream.Collectors

@Slf4j
@Integration
@Stepwise
class LabelSpec extends BaseSpec {

  @Shared
  String serialId

  @Shared
  String startDate = "2024-01-01"

  @Shared
  Map serial_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/serial_data.json"))

  @Shared
  Map ruleset_data = new groovy.json.JsonSlurper().parse(new File("src/integration-test/resources/ruleset_data.json"))

  @Shared
  Map templateConfigurations = ruleset_data.templateConfigurations

  /* Chronology labels tests
  * Approach: Template config specifies a chronology template.
  *  */
  void "Chronology rule"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.templateConfigBase
    baseConfig.enumerationRules = null
    Map respMap = doPost("/serials-management/predictedPieces/generate", [
      rulesetStatus: ruleset_data.rulesetStatus.active,
      recurrence: ruleset_data.recurrence.monthDate,
      templateConfig: baseConfig,
      owner:[
        id: serialId
      ],
      patternType: "month_date",
      startDate: startDate
    ])
    Labels labels = getLabels(baseConfig)

    then: "The system responds with a list of 12 pieces"
    assert labels.full_label == ["Monday 1 January 2024", "Thursday 1 February 2024", "Friday 1 March 2024", "Monday 1 April 2024", "Wednesday 1 May 2024", "Saturday 1 June 2024", "Monday 1 July 2024", "Thursday 1 August 2024", "Sunday 1 September 2024", "Tuesday 1 October 2024", "Friday 1 November 2024", "Sunday 1 December 2024"]
  }


  /* Approach: for testing enumeration labels, we don't need to include chronology labels.
  The base template sets ups two levels, each using numeric format.
  One level uses 1 unit and is sequence: continuous.
  The second level uses 4 units and is sequence: reset.
  The template string is like so: {{level1 level2}}

  Each test should explicitly setup the base config.
   */
  void "level 1: continuous, 1; level 2: reset, 4"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.templateConfigBaseEnumeration
    baseConfig.enumerationRules[0].ruleFormat.levels[0].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].sequence.value = "reset"
    baseConfig.enumerationRules[0].ruleFormat.levels[0].units = "1"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].units = "4"
    Labels labels = getLabels(baseConfig)

    then: "The system responds with a list of 12 pieces"
    assert labels.level1 == [1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3].collect { it as String }
    assert labels.level2 == [1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4].collect { it as String }
  }

  void "level 1: continuous, 1; level 2: reset, 1"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.templateConfigBaseEnumeration
    baseConfig.enumerationRules[0].ruleFormat.levels[0].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].sequence.value = "reset"
    baseConfig.enumerationRules[0].ruleFormat.levels[0].units = "1"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].units = "1"
    Labels labels = getLabels(baseConfig)

    then: "The system responds with a list of 12 pieces"
    assert labels.level1 == [1,2,3,4,5,6,7,8,9,10,11,12].collect { it as String }
    assert labels.level2 == [1,1,1,1,1,1,1,1,1,1,1,1].collect { it as String }
  }


  void "level 1: continuous, 4; level 2: reset, 1"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.templateConfigBaseEnumeration
    baseConfig.enumerationRules[0].ruleFormat.levels[0].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].sequence.value = "reset"
    baseConfig.enumerationRules[0].ruleFormat.levels[0].units = "4"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].units = "1"
    Labels labels = getLabels(baseConfig)

    then: "The system responds with a list of 12 pieces"
    assert labels.level1 == [1,2,3,4,5,6,7,8,9,10,11,12].collect { it as String }
    assert labels.level2 == [1,1,1,1,1,1,1,1,1,1,1,1].collect { it as String }
  }

  void "level 1: continuous, 4; level 2: continuous, 1"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.templateConfigBaseEnumeration
    baseConfig.enumerationRules[0].ruleFormat.levels[0].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[0].units = "4"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].units = "1"
    Labels labels = getLabels(baseConfig)

    then: "The system responds with a list of 12 pieces"
    assert labels.level1 == [1,2,3,4,5,6,7,8,9,10,11,12].collect { it as String }
    assert labels.level2 == [1,2,3,4,5,6,7,8,9,10,11,12].collect { it as String }
  }

  void "level 1: continuous, 1; level 2: continuous, 3"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.templateConfigBaseEnumeration
    baseConfig.enumerationRules[0].ruleFormat.levels[0].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[0].units = "1"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].units = "3"
    Labels labels = getLabels(baseConfig)

    then: "The system responds with a list of 12 pieces"
    assert labels.level1 == [1,1,1,2,2,2,3,3,3,4,4,4].collect { it as String }
    assert labels.level2 == [1,2,3,4,5,6,7,8,9,10,11,12].collect { it as String }
  }

  void "level 1: continuous, 3; level 2: continuous, 1"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.templateConfigBaseEnumeration
    baseConfig.enumerationRules[0].ruleFormat.levels[0].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[0].units = "3"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].units = "1"
    Labels labels = getLabels(baseConfig)

    then: "The system responds with a list of 12 pieces"
    assert labels.level1 == [1,2,3,4,5,6,7,8,9,10,11,12].collect { it as String }
    assert labels.level2 == [1,2,3,4,5,6,7,8,9,10,11,12].collect { it as String }
  }

  /* Multiple Enumeration Labels -
    Approach: TemplateConfig specifies a 2nd enumeration label
   */
  void "Two enumeration labels"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.templateConfigTwoEnumerations
    Labels labels = getLabels(baseConfig)

    then: "The system responds with a list of 12 pieces"
    assert labels.level1 == [1,2,3,4,5,6,7,8,9,10,11,12].collect { it as String }
    assert labels.level2 == [1,2,3,4,5,6,7,8,9,10,11,12].collect { it as String }
    assert labels.level3 == [1,2,3,4,5,6,7,8,9,10,11,12].collect { it as String } // Labels for enum2
  }

  /* Initial values cause reset to trigger on first month -
    Approach: set up a template config where the initial value and start date
    result in the level2 label starting from it's 2nd value.
    */
  void "Initial values cause reset to trigger in first month"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.initialValues
    Map respMap = doPost("/serials-management/predictedPieces/generate", [
      rulesetStatus: ruleset_data.rulesetStatus.active,
      recurrence: ruleset_data.recurrence.monthDate,
      templateConfig: baseConfig,
      owner:[
        id: serialId
      ],
      patternType: "month_date",
      startDate: "2025-05-08",
      startingValues: ruleset_data.startingValues.initialValues
    ])
    Labels labels = getLabelsUsingResponse(respMap)

    then: "The system responds with a list of 12 pieces"
    assert labels.level1 == [1,2,2,3,3,4,4,5,5,6,6,7].collect { it as String }
    assert labels.level2 == [2,1,2,1,2,1,2,1,2,1,2,1].collect { it as String }
  }

  /* Changes to Number of cycles/years -
    Approach: Set number of cycles to 2
    */
  void "Cycle repeats itself"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.templateConfigBaseEnumeration
    baseConfig.enumerationRules[0].ruleFormat.levels[0].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[0].units = "1"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].units = "1"
    Map respMap = doPost("/serials-management/predictedPieces/generate", [
      rulesetStatus: ruleset_data.rulesetStatus.active,
      recurrence: ruleset_data.recurrence.monthDate,
      templateConfig: baseConfig,
      owner:[
        id: serialId
      ],
      patternType: "month_date",
      startDate: "2025-05-08",
      numberOfCycles: 2
    ])
    Labels labels = getLabelsUsingResponse(respMap)

    then: "The system responds with a list of 12 pieces"
    assert labels.level1 == (1..24).collect { it as String }
    assert labels.level2 == (1..24).collect { it as String }
  }

  /* Enumeration Labels with an omission -
  Approach: Expect the issue number to resume after the omission at (previous_number + 1)
  */
  void "Include an omission"() {
    Map omissionRule = [:]
    omissionRule.put("rules", [ruleset_data.omission.rules.omit_by_month_april])

    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.simpleSingleEnumeration
    Map respMap = doPost("/serials-management/predictedPieces/generate", [
      rulesetStatus: ruleset_data.rulesetStatus.active,
      recurrence: ruleset_data.recurrence.monthDate,
      templateConfig: baseConfig,
      omission: omissionRule,
      owner:[
        id: serialId
      ],
      patternType: "month_date",
      startDate: "2025-07-09",
    ])
    Labels labels = getLabelsUsingResponse(respMap)

    then: "The system responds with a list of 12 pieces"
    assert labels.full_label == [1,2,3,4,5,6,7,8,null,9,10,11].collect { it as String }
  }

  /* Enumeration Labels with a combination -
  Approach: Expect the issue number to resume after the combination at (previous_number + number_combined)
  */
  void "Include a combination"() {
    Map combinationRule = [:]
    combinationRule.put("rules", [ruleset_data.combination.rules.combine_by_issue_2]) // combines issues 2 for 2 issues (i.e. issues 2 and 3)

    when: "We ask the system to generate predicted pieces"
    def baseConfig = templateConfigurations.simpleSingleEnumeration
    Map respMap = doPost("/serials-management/predictedPieces/generate", [
      rulesetStatus: ruleset_data.rulesetStatus.active,
      recurrence: ruleset_data.recurrence.monthDate,
      templateConfig: baseConfig,
      combination: combinationRule,
      owner:[
        id: serialId
      ],
      patternType: "month_date",
      startDate: "2025-07-09",
    ])
    Labels labels = getLabelsUsingResponse(respMap)

    then: "The system responds with a list of 12 pieces"
    assert labels.full_label == [1,2,4,5,6,7,8,9,10,11,12].collect { it as String }
  }

  class Labels {
    List full_label
    List level1
    List level2
    List level3

    @Override
    public String toString() {
      return "Labels{" +
        "full_label=" + full_label +
        "level1=" + level1 +
        ", level2=" + level2 +
        ", level3=" + level3 +
        '}';
    }
  }

  private Labels getLabelsUsingResponse(Map respMap) {
    List full_labels = []
    respMap.pieces.stream()
      .sorted(Comparator.comparing(
        // Sort pieces by date. If the piece has been combined, take the date from the first combined piece.
        piece -> {
          String dateString = (String) piece.get("date")
          if (!dateString && piece.combinationOrigins)  {
            dateString = piece.recurrencePieces[0].date
          }
          LocalDate.parse(dateString)
        }
      ))
      .forEach(piece -> {
        full_labels.add(piece.label?.trim())
      });

    // The goal is to go from [[1, 1], [1, 2], [1, 3]] to [1, 1, 1], [1, 2, 3]
    // i.e. to separate label 1 and 2 into two separate lists.
    // NOTE: this ASSUMES that enumeration template strings come in the form {enum1.level1 enum1.level2 enum2.level1}
    def transposedLabels = new Labels()

    try {
      def transposedList = respMap.pieces.stream()
        .sorted(Comparator.comparing(
          piece -> LocalDate.parse((String) piece.get("date"))
        ))
        .map(piece -> piece.label)
        .map(label -> label.split(' ').toList())
        .collect(Collectors.toList()).transpose();

      transposedLabels.level1 = transposedList[0]
      transposedLabels.level2 = transposedList[1]
      transposedLabels.level3 = transposedList[2]

    } catch (Exception e) {
      log.info("Null present in labels due to omission or combination rule, ignoring.")
    }
    transposedLabels.full_label = full_labels

    return transposedLabels
  }

  private Labels getLabels(Object baseConfig) {
    Map respMap = doPost("/serials-management/predictedPieces/generate", [
      rulesetStatus: ruleset_data.rulesetStatus.active,
      recurrence: ruleset_data.recurrence.monthDate,
      templateConfig: baseConfig,
      owner:[
        id: serialId
      ],
      patternType: "month_date",
      startDate: startDate
    ])

    return getLabelsUsingResponse(respMap)
  }
}

