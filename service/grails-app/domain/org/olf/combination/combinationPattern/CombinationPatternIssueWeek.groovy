package org.olf.combination.combinationPattern

import org.olf.combination.CombinationRule
import org.olf.internalPiece.InternalPiece
import org.olf.internalPiece.InternalRecurrencePiece

import java.time.LocalDate
import java.time.temporal.ChronoField

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternIssueWeek extends CombinationPattern implements MultiTenant<CombinationPatternIssueWeek> {

  Integer issue

  Integer week

  static mapping = {
    issue column: 'cpiw_issue'
     week column: 'cpiw_week'
  }

  static constraints = {
    issue nullable: false
     week nullable: false
  }

  public static boolean compareDate(CombinationRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    ArrayList<InternalPiece> weekGroup = InternalPiece.conditionalGroupRecurrencePieces(internalPieces){ip ->
      return ip.date.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == rule.pattern.week && 
      ip.date.get(ChronoField.YEAR) == date.get(ChronoField.YEAR)
    }
    if(weekGroup.size() < rule?.pattern?.issue){
      return false
    }

    Integer startIndex = InternalPiece.findIndexFromDate(internalPieces, weekGroup.sort{a,b -> a.date <=> b.date}[rule?.pattern?.issue - 1].date)
    Integer index = InternalPiece.findIndexFromDate(internalPieces, date)
    Integer endIndex = startIndex + rule?.issuesToCombine - 1
    return index >= startIndex  && index <= endIndex
  }
}
