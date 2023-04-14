package org.olf.combination.combinationPattern

import org.olf.combination.CombinationRule

import grails.gorm.MultiTenant

public abstract class CombinationPattern implements MultiTenant<CombinationPattern> {
  String id
  CombinationRule owner

  static mapping = {
         id column: 'cp_id', generator: 'uuid2', length: 36
      owner column: 'cp_owner_fk'
    version column: 'cp_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
