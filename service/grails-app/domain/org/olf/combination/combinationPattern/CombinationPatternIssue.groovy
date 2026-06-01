package org.olf.combination.combinationPattern

import org.olf.combination.CombinationRule
import org.olf.internalPiece.InternalPiece

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternIssue extends CombinationPattern implements MultiTenant<CombinationPatternIssue> {

  Integer issue

  static mapping = {
    issue column: 'cpi_issue'
  }

  static constraints = {
    issue nullable: false
  }

  // Comparing the issue value to the index value of the dates array (+1 to since arrays initialise at 0)
  public static boolean compareDate(CombinationRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    Integer index = InternalPiece.findIndexFromDate(internalPieces, date)
    //looks up how many issues make up one year per cycle
    Integer issuesPerCycle = rule?.owner?.owner?.recurrence?.issues
    if (issuesPerCycle == null || issuesPerCycle <= 1) {
      // fallback to absolute index if there is no meaningful cycle
      Integer startIndex = rule.pattern.issue - 1
      Integer endIndex = startIndex + (rule.issuesToCombine - 1)
      return index >= startIndex && index <= endIndex
    }

    if (rule?.pattern?.issue > issuesPerCycle) {
      // fallback to absolute index if the issue number is greater than the cycle length
      Integer startIndex = rule.pattern.issue - 1
      Integer endIndex = startIndex + (rule.issuesToCombine - 1)
      return index >= startIndex && index <= endIndex
    }

    //startIndex takes the rule eg:issue 11 and shifts it forward based on the current cycle
    Integer cycle = index / issuesPerCycle
    Integer startIndex = (rule?.pattern?.issue - 1) + (cycle * issuesPerCycle)
    //startIndex takes the rule e.g., "Issue 11" and shifts it forward based on the current cycle
    Integer endIndex = startIndex + (rule?.issuesToCombine - 1)
    //checks if the current piece falls within that specific cycle's combination range
    return index >= startIndex && index <= endIndex
  }
}
