package org.olf.label.labelStyle

import org.olf.label.LabelRule

import grails.gorm.MultiTenant

public abstract class LabelStyle implements MultiTenant<LabelStyle> {
  String id
  LabelRule owner

  static mapping = {
         id column: 'ls_id', generator: 'uuid2', length: 36
      owner column: 'ls_owner_fk'
    version column: 'ls_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
