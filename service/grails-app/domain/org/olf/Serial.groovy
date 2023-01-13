package org.olf

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import org.hibernate.Session
import org.hibernate.internal.SessionImpl

import groovy.sql.Sql


@GrailsCompileStatic
class Serial implements MultiTenant<Serial> {

  String id
  Date lastUpdated
  Date dateCreated

  static hasOne = [
    orderLine: SerialOrderLine
  ]

  static mappedBy = [
    orderLine: 'owner',
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
