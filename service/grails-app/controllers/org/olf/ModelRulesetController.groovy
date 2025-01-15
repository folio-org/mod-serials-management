package org.olf

import org.olf.ModelRuleset

import com.k_int.okapi.OkapiTenantAwareController

import grails.rest.*
import grails.converters.*
import org.grails.web.json.JSONObject
import grails.gorm.transactions.Transactional
import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j

@Slf4j
@CurrentTenant
class ModelRulesetController extends OkapiTenantAwareController<ModelRulesetController> {

  ModelRulesetController(){
    super(ModelRuleset)
  }

  @Override
  def save() {
    ModelRuleset.withTransaction {
      def data = getObjectToBind()

      SerialRuleset ruleset = new SerialRuleset(data?.serialRuleset).save(failOnError: true)

      ModelRuleset modelRuleset = new ModelRuleset([
        name: data?.name,
        description: data?.description,
        exampleLabel: data?.exampleLabel,
        modelRulesetStatus: data?.modelRulesetStatus
      ])

      modelRuleset.setSerialRuleset(ruleset)
      modelRuleset.save(failOnError: true)

      respond modelRuleset
    }
  }
 }