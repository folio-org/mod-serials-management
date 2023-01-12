package org.olf

import grails.gorm.MultiTenant

class Serial implements MultiTenant<Serial> {

  String id
  Date lastUpdated
  Date dateCreated

  static hasOne = [
    orderLine: SerialOrderLine
  ]

  static mapping = {
                 id column: 's_id', generator: 'uuid2', length: 36
        lastUpdated column: 's_last_updated'
        dateCreated column: 's_date_created'
            version column: 's_version'
         orderLine cascade: 'all-delete-orphan'
  }
  
  static constraints = {
        orderLine nullable: true
  }   
}
