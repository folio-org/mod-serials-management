package org.olf.label.labelFormat

import org.olf.label.labelStyle.LabelStyle

import grails.gorm.MultiTenant

public abstract class LabelFormat implements MultiTenant<LabelFormat> {
  String id
  LabelStyle owner

  static mapping = {
         id column: 'lf_id', generator: 'uuid2', length: 36
      owner column: 'lf_owner_fk'
    version column: 'lf_version'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
  }
}
