package org.olf.templateConfig.templateMetadataRuleFormat

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class EnumerationCyclicalLevelTMRF implements MultiTenant<EnumerationCyclicalLevelTMRF> {

  String id
  Integer units

  // TODO shoudld be dynamically assigned refdata
  String selectedValue

  String internalNote


  static belongsTo = [
    owner: EnumerationCyclicalTMRF
  ]

  // static mapping = {
  //   id column: 'el_id', generator: 'uuid2', length: 36
  //   owner column: 'el_owner_fk'
  //   version column: 'el_version'
  //   units column: 'el_units'
  //   format column: 'el_format_fk'
  //   sequence column: 'el_sequence_fk'
  //   internalNote column: 'el_internal_note'
  // }
  
  // // FIXME Remove nullable constraint from migrations

  // static constraints = {
  //   owner(nullable:false, blank:false);
  //   units nullable: false
  //   format nullable: false
  //   sequence nullable: false
  //   internalNote nullable: true
  // }   
}

