package org.olf

import grails.gorm.transactions.Transactional

import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.k_int.web.toolkit.refdata.RefdataValue


@Transactional
class SerialRulesetService {

  List<Integer> countPieceSets(String rulesetId) {
    return PredictedPieceSet.executeQuery("""
      SELECT COUNT(pps.id) from PredictedPieceSet as pps WHERE pps.ruleset.id = :rulesetId
    """.toString(), [rulesetId: rulesetId,])
  }

  String findActive(String serialId) {
    return SerialRuleset.executeQuery("""
      SELECT id from SerialRuleset WHERE owner.id = :serialId AND rulesetStatus.value = :active
    """.toString(), [serialId: serialId, active: 'active'])[0]
  }

  SerialRuleset updateRulesetStatus(String rulesetId, String rulesetStatus) {
    final RefdataValue updatedStatus = SerialRuleset.lookupRulesetStatus(rulesetStatus)
    SerialRuleset ruleset = SerialRuleset.findById(rulesetId)

    ruleset.rulesetStatus = updatedStatus
    ruleset.save(failOnError: true)

    return ruleset
  }
}