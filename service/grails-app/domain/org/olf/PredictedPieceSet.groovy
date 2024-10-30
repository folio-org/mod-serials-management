package org.olf

import org.olf.recurrence.Recurrence
import org.olf.internalPiece.InternalPiece
import org.olf.internalPiece.templateMetadata.TemplateMetadata

import java.time.LocalDate

import javax.persistence.Transient

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import grails.gorm.multitenancy.Tenants

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

  static mapping = {
    id column: 'pps_id', generator: 'uuid2', length: 36
    lastUpdated column: 'pps_last_updated'
    dateCreated column: 'pps_date_created'
    version column: 'pps_version'
    startDate column: 'pps_start_date'
    note column: 'pps_note'

    pieces cascade: 'all-delete-orphan'
  }
  
  static constraints = {
    lastUpdated nullable: true
    dateCreated nullable: true
    note nullable: true
    startDate nullable: false
    pieces nullable: false
    ruleset nullable: false
  }

  @Transient
  public String getTitle() {
    Tenants.withCurrent {
      String title = (SerialOrderLine.executeQuery("""
        SELECT title FROM SerialOrderLine sol
        WHERE sol.owner.id = :owner
        """,
        [owner: ruleset?.owner?.id]
      ) ?: [])[0];
      return title;
    }
  }
}
