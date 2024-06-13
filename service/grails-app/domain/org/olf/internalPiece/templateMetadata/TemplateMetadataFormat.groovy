package org.olf.internalPiece.templateMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

public abstract class TemplateMetadataFormat implements MultiTenant<TemplateMetadataFormat> {
  String id


  static mapping = {
    id column: 'tmf_id', generator: 'uuid2', length: 36
    owner column: 'tmf_owner_fk'
    version column: 'tmf_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
