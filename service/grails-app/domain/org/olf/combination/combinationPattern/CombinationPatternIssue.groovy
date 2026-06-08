package org.olf.combination.combinationPattern

import org.olf.combination.CombinationRule
import org.olf.internalPiece.InternalPiece

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import java.time.temporal.ChronoField

public class CombinationPatternIssue extends CombinationPattern implements MultiTenant<CombinationPatternIssue> {

  Integer issue

  static mapping = {
    issue column: 'cpi_issue'
  }

  static constraints = {
    issue nullable: false
  }

  // Comparing the issue value to the index value of the dates array (+1 to since arrays initialise at 0)
  public static boolean compareDate( CombinationRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces, Integer issuesPerCycle ) {
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

    Integer startIssue = rule?.pattern?.issue
    Integer endIssue = startIssue + rule?.issuesToCombine - 1

    return issueInCycle >= startIssue &&
           issueInCycle <= endIssue
}
}
