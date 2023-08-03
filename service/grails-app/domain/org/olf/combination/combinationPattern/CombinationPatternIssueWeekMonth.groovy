package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import org.olf.combination.CombinationRule
import org.olf.internalPiece.InternalPiece
import org.olf.internalPiece.InternalRecurrencePiece

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternIssueWeekMonth extends CombinationPattern implements MultiTenant<CombinationPatternIssueWeekMonth> {

  Integer issue

  Integer week

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
    issue column: 'cpiwm_issue'
     week column: 'cpiwm_week'
    month column: 'cpiwm_month_fk'
  }

  static constraints = {
    issue nullable: false
     week nullable: false
    month nullable: false
  }

  public static boolean compareDate(CombinationRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    ArrayList<InternalPiece> weekMonthGroup = InternalPiece.conditionalGroupRecurrencePieces(internalPieces){ip ->
      return ip.date.getMonth().toString() == rule.pattern.month.value.toUpperCase() &&
      ip.date.get(ChronoField.ALIGNED_WEEK_OF_MONTH) == rule.pattern.week && 
      ip.date.get(ChronoField.YEAR) == date.get(ChronoField.YEAR)
    }
    if(weekMonthGroup.size() < rule?.pattern?.issue){
      return false
    }

    Integer startIndex = InternalPiece.findIndexFromDate(internalPieces, weekMonthGroup.sort{a,b -> a.date <=> b.date}[rule?.pattern?.issue - 1].date)
    Integer index = InternalPiece.findIndexFromDate(internalPieces, date)
    Integer endIndex = startIndex + rule?.issuesToCombine - 1
    return index >= startIndex  && index <= endIndex
  }
}
