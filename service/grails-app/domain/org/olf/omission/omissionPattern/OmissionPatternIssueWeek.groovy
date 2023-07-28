package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import org.olf.omission.OmissionRule
import org.olf.internalPiece.*

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternIssueWeek extends OmissionPattern implements MultiTenant<OmissionPatternIssueWeek> {

  Integer issue

  Integer week

  static mapping = {
    issue column: 'opiw_issue'
     week column: 'opiw_week'
  }

  static constraints = {
    issue nullable: false
     week nullable: false
  }

  // Initially group all issues that fall within a specific week of year and specific year then compare the index with issue field value
  public static boolean compareDate(OmissionRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    ArrayList<InternalPiece> weekGroup = internalPieces.findAll{x -> 
      (x instanceof InternalRecurrencePiece || x instanceof InternalOmissionPiece) && 
      x.date.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == date.get(ChronoField.ALIGNED_WEEK_OF_YEAR) && 
      x.date.get(ChronoField.YEAR) == date.get(ChronoField.YEAR)
    }
    return weekGroup.get(rule?.pattern?.issue - 1)?.date == date &&
           weekGroup.get(rule?.pattern?.issue - 1)?.date?.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == rule?.pattern?.week
  }
}
