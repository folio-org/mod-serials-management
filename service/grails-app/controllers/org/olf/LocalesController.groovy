package org.olf

import com.k_int.okapi.OkapiTenantAwareController

import grails.gorm.multitenancy.CurrentTenant
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

import java.util.regex.Pattern
import java.util.Locale

@Slf4j
@CurrentTenant
class LocalesController {

  def getLocales() {
    Locale[] locales = Locale.getAvailableLocales();
    ArrayList<Map> localesList = locales.collect { Locale locale ->
      {[ 
        label: locale.getDisplayName(),
        value: locale.toString()
      ]}
    }.sort { it.label }
    
    respond localesList
  }
 }