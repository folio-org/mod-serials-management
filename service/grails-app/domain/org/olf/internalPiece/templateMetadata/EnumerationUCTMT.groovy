package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationUCTMT extends UserConfiguredTemplateMetadataType implements MultiTenant<EnumerationUCTMT> {

  String value
  Set<EnumerationLevelUCTMT> levels

  static hasMany = [
    levels: EnumerationLevelUCTMT,
  ]

  static mapping = {
    value column: 'euctmt_value'
    levels cascade: 'all-delete-orphan', sort: 'index', order: 'asc'
  }
  
  static constraints = {
    value nullable: true
    levels nullable: true
  }
}
