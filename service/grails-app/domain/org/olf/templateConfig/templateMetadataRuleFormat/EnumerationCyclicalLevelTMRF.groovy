package org.olf.templateConfig.templateMetadataRuleFormat

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class EnumerationCyclicalLevelTMRF implements MultiTenant<EnumerationCyclicalLevelTMRF> {

  String id
  Integer units

  // TODO shoudld be dynamically assigned refdata
  // FIXME Should also have a more district name than just 'Value'
  String value

  String internalNote


  static belongsTo = [
    owner: EnumerationCyclicalTMRF
  ]

  static mapping = {
    id column: 'ecltmrf_id', generator: 'uuid2', length: 36
    owner column: 'ecltmrf_owner_fk'
    version column: 'ecltmrf_version'
    units column: 'ecltmrf_units'
    value column: 'ecltmrf_value'
    internalNote column: 'ecltmrf_internal_note'
  }
  
  static constraints = {
    owner(nullable:false, blank:false);
    units nullable: false
    value nullable: false
    internalNote nullable: true
  }   
}

