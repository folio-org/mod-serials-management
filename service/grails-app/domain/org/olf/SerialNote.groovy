package org.olf

import grails.gorm.MultiTenant

class SerialNote implements MultiTenant<SerialNote> {

  String id

  String note

  static belongsTo = [
    owner: Serial
  ]

  static mapping = {
    id column: 'sn_id', generator: 'uuid2', length: 36
    owner column: 'sn_owner_fk'
    note column: 'sn_note'
    version column: 'sn_version'
  }
  
  static constraints = {
    owner(nullable:false, blank:false);
    note nullable: true
  }   
}

