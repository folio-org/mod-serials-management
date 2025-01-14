package org.olf

import grails.gorm.MultiTenant

class RulesetOwner implements MultiTenant<RulesetOwner> {

  String id
  Date lastUpdated
  Date dateCreated

  static mapping = {
    id column: 'ro_id', generator: 'uuid2', length: 36
    lastUpdated column: 'ro_last_updated'
    dateCreated column: 'ro_date_created'
    version column: 'ro_version'
    tablePerHierarchy false
  }
  
  static constraints = {
    lastUpdated nullable: true
    dateCreated nullable: true
  }   
}
