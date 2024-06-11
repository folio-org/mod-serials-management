package org.olf.startingValueMetadata.startingValueMetadataFormat

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationLevelSVMF implements MultiTenant<EnumerationLevelSVMF> {
  String id
  String value
  Integer Index

  static belongsTo = [
    owner: EnumerationSVMF
  ]

  static mapping = {
    id column: 'elsvmf_id', generator: 'uuid2', length: 36
    owner column: 'elsvmf_owner_fk'
    version column: 'elsvmf_version'
    index column: 'elsvmf_index'
    value column: 'elsvmf_value'
  }

  static constraints = {
    owner(nullable:false, blank:false);
    index nullable: false
    value nullable: false
  }
}
