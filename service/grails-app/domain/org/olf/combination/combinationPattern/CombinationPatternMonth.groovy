package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import java.time.Month
import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternMonth extends CombinationPattern implements MultiTenant<CombinationPatternMonth> {

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
    month column: 'cpm_month_fk'
  }

  static constraints = {
    month nullable: false
  }

  // If fields are a range, compare to see if numerical value of month falls within these
  // If not, compare month to month of year
  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
      return date?.get(ChronoField.MONTH_OF_YEAR) == Month.valueOf(rule.pattern.month.value.toUpperCase()).getValue() 
  }
}
