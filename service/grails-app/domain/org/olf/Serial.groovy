package org.olf

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import groovy.sql.Sql


@GrailsCompileStatic
class Serial implements MultiTenant<Serial> {

  String id
  Date lastUpdated
  Date dateCreated
  String description

  @CategoryId(defaultInternal=true)
  @Defaults(['Active', 'Closed'])
  RefdataValue serialStatus

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
       serialStatus column: 's_serial_status'
        description column: 's_description'
            version column: 's_version'
         orderLine cascade: 'all-delete-orphan'
  }
  
  static constraints = {
        orderLine nullable: true
      lastUpdated nullable: true
      dateCreated nullable: true
     serialStatus nullable: true
      description nullable: true
  }   
}
