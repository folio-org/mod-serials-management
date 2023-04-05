package org.olf.omission.omissionPattern

import org.olf.omission.OmissionRule

import grails.gorm.MultiTenant

public abstract class OmissionPattern implements MultiTenant<OmissionPattern> {
  String id
  OmissionRule owner

  static mapping = {
         id column: 'op_id', generator: 'uuid2', length: 36
      owner column: 'op_owner_fk'
    version column: 'op_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
