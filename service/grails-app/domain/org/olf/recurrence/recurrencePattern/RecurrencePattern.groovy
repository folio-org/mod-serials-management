package org.olf

import grails.gorm.MultiTenant

public class RecurrencePattern implements MultiTenant<RecurrencePattern> {
  String id
  RecurrenceRule owner

  static mapping = {
       id column: 'rep_id', generator: 'uuid2', length: 36
    owner column: 'rep_owner_fk'
  }

  static constraints = {
       id nullable: false
    owner nullable: false
  }
}
