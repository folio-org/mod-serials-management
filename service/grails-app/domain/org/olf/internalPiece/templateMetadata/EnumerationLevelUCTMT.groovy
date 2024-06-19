package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationLevelUCTMT implements MultiTenant<EnumerationLevelUCTMT> {
  String id
  String value
  Integer index

  static belongsTo = [
    owner: EnumerationUCTMT
  ]

  static mapping = {
    id column: 'euctmt_id', generator: 'uuid2', length: 36
    owner column: 'euctmt_owner_fk'
    version column: 'euctmt_version'
    index column: 'euctmt_index'
    value column: 'euctmt_value'
  }

  static constraints = {
    owner(nullable:false, blank:false);
    index nullable: false
    value nullable: false
  }
}
