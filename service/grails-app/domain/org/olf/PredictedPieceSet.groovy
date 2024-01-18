package org.olf

import org.olf.recurrence.Recurrence
import org.olf.internalPiece.InternalPiece

import java.time.LocalDate
import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class PredictedPieceSet implements MultiTenant<PredictedPieceSet> {

  String id
  Date lastUpdated
  Date dateCreated

  LocalDate startDate

  Serial serial

  String note

  static hasMany = [
    pieces: InternalPiece
  ]

  static mappedBy = [
    pieces: 'owner',
    // serial: 'predictedPieceSet',
    // ruleset: 'predictedPieceSet',
  ]

  static mapping = {
    id column: 'pps_id', generator: 'uuid2', length: 36
    lastUpdated column: 'pps_last_updated'
    dateCreated column: 'pps_date_created'
    version column: 'pps_version'
    startDate column: 'pps_start_date'
    note column: 'pps_note'

    pieces cascade: 'all-delete-orphan'
    serial cascade: 'all-delete-orphan'
  }
  
  static constraints = {
    lastUpdated nullable: true
    dateCreated nullable: true
    note nullable: true
    startDate nullable: false
    pieces nullable: false
    serial nullable: false
  }   
}
