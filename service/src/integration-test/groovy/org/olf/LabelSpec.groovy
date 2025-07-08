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

  /* Chronology labels tests
  * Approach:
  *  */
  void "level 1: continuous, 3; level 2: continuous, 1"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = ruleset_data.templateConfigBase
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

    then: "The system responds with a list of 12 pieces"
    true // Todo
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
    def baseConfig = ruleset_data.templateConfigBaseEnumeration
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
    def baseConfig = ruleset_data.templateConfigBaseEnumeration
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
    def baseConfig = ruleset_data.templateConfigBaseEnumeration
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
    def baseConfig = ruleset_data.templateConfigBaseEnumeration
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
    def baseConfig = ruleset_data.templateConfigBaseEnumeration
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
    def baseConfig = ruleset_data.templateConfigBaseEnumeration
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
    Approach:
   */

  /* Changes to start dates -
  Approach:
 */

  /* Changes to initial level label values -
    Approach:
    */

  /* Changes to Number of cycles/years -
    Approach:
    */

  /* Enumeration Labels with an omission -
  Approach: Expect the issue number to resume after the omission at (previous_number + 1)
  */

  /* Enumeration Labels with a combination -
  Approach: Expect the issue number to resume after the combination at (previous_number + number_combined)
  */

  class Labels {
    List level1
    List level2

    @Override
    public String toString() {
      return "Labels{" +
        "level1=" + level1 +
        ", level2=" + level2 +
        '}';
    }
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

    respMap.pieces.stream()
      .sorted(Comparator.comparing(
        piece -> LocalDate.parse((String) piece.get("date"))
      ))
      .forEach(piece -> log.info(piece.label));

    // The goal is to go from [[1, 1], [1, 2], [1, 3]] to [1, 1, 1], [1, 2, 3]
    // i.e. to separate label 1 and 2 into two separate lists.
    def transposedList = respMap.pieces.stream()
      .sorted(Comparator.comparing(
        piece -> LocalDate.parse((String) piece.get("date"))
      ))
      .map(piece -> piece.label)
      .map(label -> label.split(' ').toList())
      .collect(Collectors.toList()).transpose();

    def transposedLabels = new Labels()
    transposedLabels.level1 = transposedList[0]
    transposedLabels.level2 = transposedList[1]

    log.info(respMap.pieces.toString())
    log.info(transposedLabels.toString())

    return transposedLabels
  }
}

