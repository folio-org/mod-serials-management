package org.olf

import org.olf.recurrence.Recurrence
import org.olf.internalPiece.InternalPiece
import org.olf.internalPiece.templateMetadata.TemplateMetadata

import java.time.LocalDate

import javax.persistence.Transient

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import grails.gorm.multitenancy.Tenants

import org.grails.orm.hibernate.cfg.GrailsHibernateUtil

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
  Integer numberOfCycles

  SerialRuleset ruleset

  // The template metadata for the first piece within a pieceset
  // prior to omission/combination rules applied
  TemplateMetadata initialPieceRecurrenceMetadata

  
  // The template metadata for the next piece within a following pieceset
  // prior to omission/combination rules applied
  TemplateMetadata continuationPieceRecurrenceMetadata

  String note

  static hasMany = [
    pieces: InternalPiece
  ]

  static mappedBy = [
    pieces: 'owner',
  ]

  static transients = [ 'title' ]

  static mapping = {
    id column: 'pps_id', generator: 'uuid2', length: 36
    lastUpdated column: 'pps_last_updated'
    dateCreated column: 'pps_date_created'
    version column: 'pps_version'
    startDate column: 'pps_start_date'
    numberOfCycles column: 'pps_number_of_cycles'
    note column: 'pps_note'

    pieces cascade: 'all-delete-orphan'
  }
  
  static constraints = {
    lastUpdated nullable: true
    dateCreated nullable: true
    startDate nullable: false
    numberOfCycles nullable: true
    note nullable: true
    pieces nullable: false
    ruleset nullable: false
  }

  String getTitle() {
    String title = '';
    SerialOrderLine.withNewTransaction {
      Serial owner = GrailsHibernateUtil.unwrapIfProxy(ruleset?.owner);
      Tenants.withCurrent {
        title = (SerialOrderLine.executeQuery("""
          SELECT title FROM SerialOrderLine sol
          WHERE sol.owner.id = :owner
          """,
          [owner: owner?.id]
        ) ?: [])[0] ?: '';
      }
    }

    return title;
  }
}
