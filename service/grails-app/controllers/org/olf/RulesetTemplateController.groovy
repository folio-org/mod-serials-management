package org.olf

import org.olf.RulesetTemplate

import com.k_int.okapi.OkapiTenantAwareController

import grails.rest.*
import grails.converters.*
import org.grails.web.json.JSONObject
import grails.gorm.transactions.Transactional
import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j

@Slf4j
@CurrentTenant
class RulesetTemplateController extends OkapiTenantAwareController<RulesetTemplateController> {

  RulesetTemplateController(){
    super(RulesetTemplate)
  }

  // @Transactional
  @Override
  def save() {
    RulesetTemplate.withTransaction {
      def data = getObjectToBind()

      SerialRuleset ruleset = new SerialRuleset(data?.serialRuleset).save(failOnError: true)

      RulesetTemplate rulesetTemplate = new RulesetTemplate([
        name: data?.name,
        description: data?.description,
        exampleLabel: data?.exampleLabel,
        rulesetTemplateStatus: data?.rulesetTemplateStatus
        // serialRuleset: ruleset
      ])

      rulesetTemplate.setSerialRuleset(ruleset)
      rulesetTemplate.save(failOnError: true)



      // SerialRuleset ruleset = new SerialRuleset(data?.serialRuleset)
      // ruleset.owner = rulesetTemplate.id
      // ruleset.save(failOnError: true)
      // rulesetTemplate.serialRuleset = ruleset
      
      // rulesetTemplate.save(failOnError: true)

      respond rulesetTemplate
    }
  }
 }