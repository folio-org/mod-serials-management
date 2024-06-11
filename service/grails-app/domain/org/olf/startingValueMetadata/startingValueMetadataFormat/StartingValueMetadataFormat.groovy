package org.olf.startingValue

package org.olf.startingValue.startingValueMetadataFormat.StartingValueMetadataFormat

import java.time.LocalDate

import grails.gorm.MultiTenant

public abstract class StartingValueMetadataFormat implements MultiTenant<StartingValues> {
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
