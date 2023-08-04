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
    Integer endIndex = (rule?.pattern?.issue -1) + (rule?.issuesToCombine - 1)
    return index >= (rule?.pattern?.issue -1) && index <= endIndex
  }
}
