package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternDayWeekday extends CombinationPattern implements MultiTenant<CombinationPatternDayWeekday> {

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
    weekday column: 'cpdwd_weekday_fk'
  }

  static constraints = {
    weekday nullable: false
  }

  // Comparing weekday to day of week
  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
    return (rule?.pattern?.weekday?.value?.toUpperCase() == date.getDayOfWeek().toString())
  }
}
