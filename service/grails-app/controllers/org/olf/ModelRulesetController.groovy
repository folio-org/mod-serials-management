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

      // TODO We should be naming this serialRuleset, it should just be ruleset
      // Instantiate a new SerialRuleset here, defining each of the fields
      // This is to ensure that owner is not passed when instantiating the ruleset
      // This could be done using bindUsingWhen ref at the ruleset level but this is less of pain
      SerialRuleset ruleset = new SerialRuleset([
        rulesetNumber: data?.serialRuleset?.rulesetNumber,
        description: data?.serialRuleset?.description,
        rulesetStatus: data?.serialRuleset?.rulesetStatus,
        recurrence: data?.serialRuleset?.recurrence,
        omission: data?.serialRuleset?.omission,
        combination: data?.serialRuleset?.combination,
        templateConfig: data?.serialRuleset?.templateConfig,
      ])

      // Now instantiate a new ModelRuleset
      ModelRuleset modelRuleset = new ModelRuleset([
        name: data?.name,
        description: data?.description,
        exampleLabel: data?.exampleLabel,
        modelRulesetStatus: data?.modelRulesetStatus
      ])

      // Bind serial ruleset to model ruleset so that owner is set correctly
      modelRuleset.setSerialRuleset(ruleset)
      modelRuleset.save(failOnError: true)

      respond modelRuleset
    }
  }
 }