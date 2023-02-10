package org.olf.recurrence.recurrencePattern

import org.olf.recurrence.RecurrenceRule

import grails.gorm.MultiTenant

public class RecurrencePattern implements MultiTenant<RecurrencePattern> {
  String id
  RecurrenceRule owner

  static mapping = {
         id column: 'rp_id', generator: 'uuid2', length: 36
      owner column: 'rp_owner_fk'
    version column: 'rp_version'

    tablePerHierarchy false
  }

  static constraints = {
       id nullable: false
    owner nullable: false
  }
}
