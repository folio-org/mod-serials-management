package org.olf.internalPiece.templateMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

public abstract class UserConfiguredTemplateMetadataType implements MultiTenant<UserConfiguredTemplateMetadataType> {
  String id
  UserConfiguredTemplateMetadata owner

  static mapping = {
    id column: 'uctmt_id', generator: 'uuid2', length: 36
    owner column: 'uctmt_owner_fk'
    version column: 'uctmt_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
