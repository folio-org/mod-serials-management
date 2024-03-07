package org.olf

import grails.gorm.transactions.Transactional

import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.k_int.web.toolkit.refdata.RefdataValue


@Transactional
class SerialRulesetService {

  String findActive(String serialId) {
    return SerialRuleset.executeQuery("""
      SELECT id from SerialRuleset WHERE owner.id = :serialId AND rulesetStatus.value = :active
    """.toString(), [serialId: serialId, active: 'active'])[0]
  }

  SerialRuleset createRuleset(Map rulesetParams) {
    SerialRuleset ruleset = new SerialRuleset(rulesetParams).save(flush:true, failOnError: true);
    return ruleset;
  }

  public void deprecateRuleset(rulesetId) {
    final RefdataValue deprecatedStatus = SerialRuleset.lookupRulesetStatus('deprecated')
    SerialRuleset activeRuleset = SerialRuleset.findById(activeRulesetId)
    bindData(activeRuleset, [rulesetStatus: deprecatedStatus])
    updateResource activeRuleset
  }
}