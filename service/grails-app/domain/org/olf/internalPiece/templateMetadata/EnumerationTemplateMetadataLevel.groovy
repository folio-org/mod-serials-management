package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

// TODO Rename to EnumerationLevelTemplateMetadata
public class EnumerationTemplateMetadataLevel implements MultiTenant<EnumerationTemplateMetadataLevel> {
  String id
  String value
  Integer Index

  static belongsTo = [
    owner: EnumerationTemplateMetadata
  ]

  static mapping = {
    id column: 'eltm_id', generator: 'uuid2', length: 36
    owner column: 'eltm_owner_fk'
    version column: 'eltm_version'
    index column: 'eltm_index'
    value column: 'eltm_value'
  }

  static constraints = {
    owner(nullable:false, blank:false);
    index nullable: false
    value nullable: false
  }
}
