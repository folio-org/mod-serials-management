package org.olf.templateConfig.templateMetadataRuleFormat

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class EnumerationNumericLevelTMRF implements MultiTenant<EnumerationNumericLevelTMRF> {

  String id
  Integer units

  @CategoryId(value="EnumerationNumericLevelTMRF.Format", defaultInternal=true)
  @Defaults(['Ordinal', 'Number', 'Roman'])
  RefdataValue format

  @CategoryId(value="EnumerationNumericLevelTMRF.Sequence", defaultInternal=true)
  @Defaults(['Reset', 'Continuous'])
  RefdataValue sequence

  String internalNote


  static belongsTo = [
    owner: EnumerationNumericTMRF
  ]

  static mapping = {
    id column: 'enltmrf_id', generator: 'uuid2', length: 36
    owner column: 'enltmrf_owner_fk'
    version column: 'enltmrf_version'
    units column: 'enltmrf_units'
    format column: 'enltmrf_format_fk'
    sequence column: 'enltmrf_sequence_fk'
    internalNote column: 'enltmrf_internal_note'
  }
  
  static constraints = {
    owner(nullable:false, blank:false);
    units nullable: false
    format nullable: false
    sequence nullable: false
    internalNote nullable: true
  }   
}

