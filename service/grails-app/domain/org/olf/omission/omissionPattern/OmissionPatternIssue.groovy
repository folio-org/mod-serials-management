package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import org.olf.omission.OmissionRule
import org.olf.internalPiece.InternalPiece

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import java.time.temporal.ChronoField

public class OmissionPatternIssue extends OmissionPattern implements MultiTenant<OmissionPatternIssue> {

  Integer issue

  static mapping = {
    issue column: 'opi_issue'
  }

  static constraints = {
    issue nullable: false
  }

  // Comparing the issue value to the index value of the dates array (+1 to since arrays initialise at 0)
  public static boolean compareDate( OmissionRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces,Integer issuesPerCycle) {
    Integer issueInCycle
    if (issuesPerCycle == 1) {
      ArrayList<InternalPiece> yearGroup = InternalPiece.conditionalGroupRecurrencePieces(internalPieces){ip ->
        return ip.date.get(ChronoField.YEAR) == date.get(ChronoField.YEAR)
      }
      issueInCycle = yearGroup.findIndexOf { it.date == date } + 1
    } else {
      Integer index = InternalPiece.findIndexFromDate(internalPieces, date)
      issueInCycle = (index % issuesPerCycle) + 1
    }

    return issueInCycle == rule?.pattern?.issue
}
}
