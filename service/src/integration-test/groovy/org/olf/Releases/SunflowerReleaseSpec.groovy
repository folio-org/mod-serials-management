package org.olf

import grails.testing.mixin.integration.Integration

import spock.lang.Stepwise

import groovy.util.logging.Slf4j

@Slf4j
@Integration
@Stepwise
class SunflowerReleaseSpec extends BaseSpec {

  // Refs MODSER-104
  void "Check creating a serial and then query for created serial"() {
    log.debug("Testing....")
    when: "Grab refdata category Global.Month"
      List refdataList = doGet("/serials-management/refdata", [filters: ['desc==Global.Month']])

    log.debug("Refdata list: ${refdataList}")
    then: "Response is good and we have a refdata category"
      refdataList[0].id != null
      refdataList[0].desc == 'Global.Month'
    
    when: "Post to create new empty serial with description"
      Map serialPostMap = doPost('/serials-management/serials', [
        serialStatus: 'active',
        description: 'test description'
      ])

    then: 'Response is good and we have a new serial'
      serialPostMap.id != null
      serialPostMap.description == 'test description'
      serialPostMap.serialStatus.value == 'active'

    when: 'Query for the created serial'
      String serialId = serialPostMap.id
      Map serialGetMap = doGet("/serials-management/serials/${serialId}")

    then: 'Serial found and ID matches returned one from before'
      serialGetMap.id == serialId
      serialGetMap.description == 'test description'
      serialGetMap.serialStatus.value == 'active'

    when: "Post to create new ruleset with daily recurrence and active status"
      log.debug("Create new rulset");

      Map rulesetPostMap = doPost('/serials-management/rulesets', [
        rulesetStatus: 'active',
        recurrence: [
          period: "1",
          issues: "1",
          timeUnit: [
            value: "month"
          ],
          rules: [
            [
              pattern: [
                day: 1
              ],
              ordinal: "1",
              patternType: "month_date"
            ]
          ]
        ],
        templateConfig: [
          enumerationRules: [
            [
              index: "0",
              templateMetadataRuleFormat: "enumeration_textual",
              ruleFormat: [
                refdataCategory: [
                  desc: "Global.Month",
                ],
                levels: [
                  [
                    index: "1",
                    units: "1",
                    refdataValue: [
                      id: refdataList[0].values.find { it.label == "January" }.id
                    ]
                  ]
                ]
              ]
            ]
          ],
          templateString: "Test Ruleset"
        ],
        owner: [
          id: serialId
        ],
        patternType: "month_date"
      ]);

    then: "Response is good and we have a new ID and a status of active"
      rulesetPostMap.id != null
      def activeRulesetId = rulesetPostMap.id
      rulesetPostMap.rulesetStatus.value == 'active'

    when: "Grab refdata category Global.Month"
      refdataList = doGet("/serials-management/refdata", [filters: ['desc==Global.Month']])

    log.debug("Refdata list: ${refdataList}")
    then: "Response is good and we have a refdata category"
      refdataList.size() == 1
  }
}
