package org.olf

import grails.testing.mixin.integration.Integration

import spock.lang.Stepwise

import groovy.util.logging.Slf4j

@Slf4j
@Integration
@Stepwise
class SunflowerReleaseSpec extends BaseSpec {

  // Refs MODSER-104
  void "Check that passing a refdata category desc to an enumeration textual rule does not cause a new refdata category to be created"() {

    // Used to ensure test data exists and refdata values can be used later in the test
    when: "Grab refdata category Global.Month"
      List refdataList = doGet("/serials-management/refdata", [filters: ['desc==Global.Month']])
      log.debug("Refdata list: ${refdataList}")
    
    // Ensure we have the refdata category we need
    then: "Response is good and we have a refdata category"
      refdataList[0].id != null
      refdataList[0].desc == 'Global.Month'
    
    // Create a serial to assign the ruleset to
    when: "Post to create new empty serial with description"
      Map serialPostMap = doPost('/serials-management/serials', [
        serialStatus: 'active',
        description: 'test description'
      ])

    then: 'Response is good and we have a new serial'
      serialPostMap.id != null

    // Create a new serial using refdataCategory.desc and refdataValue Id from fetched values
    when: "Post to create new ruleset with daily recurrence and active status"
      String serialId = serialPostMap.id
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

    when: "Grab refdata category Global.Month"
      refdataList = doGet("/serials-management/refdata", [filters: ['desc==Global.Month']])

    then: "We still only have one refdata category"
      refdataList.size() == 1
  }
}
