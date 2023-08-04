package org.olf.combination.combinationPattern

import org.olf.combination.CombinationRule
import org.olf.internalPiece.InternalPiece

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

// TODO DECIDE IF WE'RE DOING THIS
public class CombinationPatternWeekMonth extends CombinationPattern implements MultiTenant<CombinationPatternWeekMonth> {

  Integer week

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
     week column: 'cpwm_week'
    month column: 'cpwm_month_fk'
  }

  static constraints = {
     week nullable: false
    month nullable: false
  }

  // Compare week to week of month and month to month of year
  public static boolean compareDate(CombinationRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    return (rule?.pattern?.month?.value?.toUpperCase() == date.getMonth().toString() &&
            rule?.pattern?.week == date.get(ChronoField.ALIGNED_WEEK_OF_MONTH))
    }
}
