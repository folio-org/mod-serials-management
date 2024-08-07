package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationLevelUCTMT implements MultiTenant<EnumerationLevelUCTMT> {
  String id
  String value
  Integer rawValue
  Integer index

  // Roman, Number, Ordinal - Matching EnumerationNumericLevelTMRF
  RefdataValue valueFormat

  static belongsTo = [
    owner: EnumerationUCTMT
  ]

  static mapping = {
    id column: 'eluctmt_id', generator: 'uuid2', length: 36
    owner column: 'eluctmt_owner_fk'
    version column: 'eluctmt_version'
    index column: 'eluctmt_index'
    value column: 'eluctmt_value'
    rawValue column: 'eluctmt_raw_value'
    valueFormat column: 'eluctmt_value_format_fk'
  }

  static constraints = {
    owner(nullable:false, blank:false);
    index nullable: false
    value nullable: false
    rawValue nullable: false
    valueFormat nullable: false
  }
}
