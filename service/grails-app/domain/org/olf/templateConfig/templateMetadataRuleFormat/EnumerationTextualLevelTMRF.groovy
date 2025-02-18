package org.olf.templateConfig.templateMetadataRuleFormat

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class EnumerationTextualLevelTMRF implements MultiTenant<EnumerationTextualLevelTMRF> {

  String id

  Integer index

  Integer units

  // TODO shoudld be dynamically assigned refdata
  // FIXME Should also have a more district name than just 'Value'
//  String value

  RefdataValue refdataValue

  String internalNote


  static belongsTo = [
    owner: EnumerationTextualTMRF
  ]

  static mapping = {
    id column: 'etltmrf_id', generator: 'uuid2', length: 36
    owner column: 'etltmrf_owner_fk'
    version column: 'etltmrf_version'
    index column: 'etltmrf_index'
    units column: 'etltmrf_units'
//    value column: 'etltmrf_value'
    refdataValue column: 'etlmrf_refdata_value_fk'
    internalNote column: 'etltmrf_internal_note'
  }
  
  static constraints = {
    owner(nullable:false, blank:false);
    index nullable: false
    units nullable: false
//    value nullable: false
    refdataValue nullable: false
    internalNote nullable: true
  }   
}

