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

  /* Approach: for testing enumeration labels, we don't need to include chronology labels.
  The base template sets ups two levels, each using numeric format.
  One level uses 1 unit and is sequence: continuous.
  The second level uses 4 units and is sequence: reset.
  The template string is like so: {{level1 level2}}

  Each test should explicitly setup the base config.
   */

  void "Test labelling"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = ruleset_data.templateConfigBaseEnumeration
    baseConfig.enumerationRules[0].ruleFormat.levels[0].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].sequence.value = "reset"
    baseConfig.enumerationRules[0].ruleFormat.levels[0].units = "1"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].units = "4"
    Map respMap = doPost("/serials-management/predictedPieces/generate", [
      rulesetStatus: ruleset_data.rulesetStatus.active,
      recurrence: ruleset_data.recurrence.monthDate,
      templateConfig: ruleset_data.templateConfigBaseEnumeration,
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
      .map(label -> label.split('\\s*').toList())
      .collect(Collectors.toList()).transpose();
    def transposedLabels = [:]
    transposedLabels.put("label1", transposedList[0])
    transposedLabels.put("label2", transposedList[2])

    then: "The system responds with a list of 12 pieces"
    log.info(respMap.pieces.toString())
    log.info(transposedLabels.toMapString())

    assert transposedLabels.get("label1") == [1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3].collect { it as String }
    assert transposedLabels.get("label2") == [1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4].collect { it as String }

    respMap?.pieces.size() == 12
  }

  void "Test labelling 2"() {
    when: "We ask the system to generate predicted pieces"
    def baseConfig = ruleset_data.templateConfigBaseEnumeration
    baseConfig.enumerationRules[0].ruleFormat.levels[0].sequence.value = "continuous"
    baseConfig.enumerationRules[0].ruleFormat.levels[1].sequence.value = "reset"
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
      startDate: startDate
    ])

    then: "The system responds with a list of 12 pieces"
    log.info(respMap.pieces.toString())
    respMap.pieces.stream()
      .sorted(Comparator.comparing(
        piece -> LocalDate.parse((String) piece.get("date"))
      ))
      .forEach(piece -> log.info(piece.label));
    respMap?.pieces.size() == 12
  }
}

