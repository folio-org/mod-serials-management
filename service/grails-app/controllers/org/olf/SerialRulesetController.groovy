package org.olf

import org.olf.SerialRuleset
import org.olf.SerialRulesetService

import com.k_int.okapi.OkapiTenantAwareController
import com.k_int.web.toolkit.refdata.RefdataValue

import grails.rest.*
import grails.converters.*
import grails.gorm.multitenancy.CurrentTenant

import groovy.util.logging.Slf4j

import java.util.regex.Pattern

@Slf4j
@CurrentTenant
class SerialRulesetController extends OkapiTenantAwareController<SerialRuleset> {

  SerialRulesetService serialRulesetService

  SerialRulesetController(){
    super(SerialRuleset)
  }

  def save() {
    def data = getObjectToBind()
    if(data?.rulesetStatus?.value == 'active'){
      String activeRulesetId = serialRulesetService.findActive(data?.owner?.id)
      if(activeRulesetId){
        serialRulesetService.deprecateRuleset(activeRulesetId)
      }
    }
    respond serialRulesetService.createRuleset(data)
  }
}
