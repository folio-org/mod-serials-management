package org.olf

import org.olf.SerialRuleset

import com.k_int.okapi.OkapiTenantAwareController

import grails.rest.*
import grails.converters.*
import grails.gorm.multitenancy.CurrentTenant

import groovy.util.logging.Slf4j

import java.util.regex.Pattern

@Slf4j
@CurrentTenant
class SerialRulesetController extends OkapiTenantAwareController<SerialRuleset> {

  SerialRulesetController(){
    super(SerialRuleset)
  }
}
