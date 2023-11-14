package org.olf.templateConfig.templateMetadataRuleFormat

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class EnumerationCyclicalLevelTMRF implements MultiTenant<EnumerationCyclicalLevelTMRF> {

  String id
  Integer units

  @CategoryId(value="EnumerationCyclicalLevelTMRF.Format", defaultInternal=true)
  @Defaults(['Ordinal', 'Number', 'Roman'])
  RefdataValue format

  @CategoryId(value="EnumerationCyclicalLevelTMRF.Sequence", defaultInternal=true)
  @Defaults(['Reset', 'Continuous'])
  RefdataValue sequence

  String internalNote


  static belongsTo = [
    owner: EnumerationTemplateMetadataRule
  ]

  static mapping = {
    id column: 'el_id', generator: 'uuid2', length: 36
    owner column: 'el_owner_fk'
    version column: 'el_version'
    units column: 'el_units'
    format column: 'el_format_fk'
    sequence column: 'el_sequence_fk'
    internalNote column: 'el_internal_note'
  }
  
  // FIXME Remove nullable constraint from migrations

  static constraints = {
    owner(nullable:false, blank:false);
    units nullable: false
    format nullable: false
    sequence nullable: false
    internalNote nullable: true
  }   
}

