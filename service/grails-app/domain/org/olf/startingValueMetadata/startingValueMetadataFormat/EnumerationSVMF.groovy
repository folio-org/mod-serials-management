package org.olf.startingValueMetadata.startingValueMetadataFormat

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationSVMF extends StartingValueMetadataFormat implements MultiTenant<EnumerationSVMF> {

  String value
  Set<EnumerationLevelSVMF> levels
  
  static hasMany = [
    levels: EnumerationLevelSVMF,
  ]

  static mapping = {
    value column: 'esvmf_value'
    levels cascade: 'all-delete-orphan', sort: 'index', order: 'asc'
  }
  
  static constraints = {
    value nullable: true
    levels nullable: true
  }
}
