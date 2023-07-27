package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

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
  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
    return (rule?.pattern?.month?.value?.toUpperCase() == date.getMonth().toString() &&
            Integer.parseInt(rule?.pattern?.week) == date.get(ChronoField.ALIGNED_WEEK_OF_MONTH))
    }
}
