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

public class CombinationPatternIssueMonth extends CombinationPattern implements MultiTenant<CombinationPatternIssueMonth> {

  Integer issue

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
    issue column: 'cpim_issue'
    month column: 'cpim_month_fk'
  }

  static constraints = {
    issue nullable: false
    month nullable: false
  }

  public static boolean compareDate(CombinationRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    ArrayList<InternalPiece> monthGroup = InternalPiece.conditionalGroupRecurrencePieces(internalPieces){ip ->
      return ip.date.getMonth().toString() == rule.pattern.month.value.toUpperCase() && 
      ip.date.get(ChronoField.YEAR) == date.get(ChronoField.YEAR)
    }
    if(monthGroup.size() < rule?.pattern?.issue){
      return false
    }

    InternalPiece startIssue = monthGroup.sort{a,b -> a.date <=> b.date}[rule?.pattern?.issue - 1]
    Integer startIndex = InternalPiece.findIndexFromDate(internalPieces, monthGroup.sort{a,b -> a.date <=> b.date}[rule?.pattern?.issue - 1].date)
    Integer index = InternalPiece.findIndexFromDate(internalPieces, date)
    Integer endIndex = startIndex + rule?.issuesToCombine - 1
    return index >= startIndex  && index <= endIndex
  }
}
