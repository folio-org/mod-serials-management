package org.olf.startingValueMetadata.startingValueMetadataFormat

import org.olf.startingValueMetadata.StartingValueMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

public abstract class StartingValueMetadataFormat implements MultiTenant<StartingValueMetadataFormat> {
  String id
  StartingValueMetadata owner

  static mapping = {
    id column: 'svmf_id', generator: 'uuid2', length: 36
    owner column: 'svmf_owner_fk'
    version column: 'svmf_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
