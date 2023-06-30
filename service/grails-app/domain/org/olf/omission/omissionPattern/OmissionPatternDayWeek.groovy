package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternDayWeek extends OmissionPattern implements MultiTenant<OmissionPatternDayWeek> {

  Integer week

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
      week column : 'opdw_week'
    weekday column: 'opdw_weekday_fk'
  }

  static constraints = {
       week nullable: false
    weekday nullable: false
  }

  // Comparing week field to week of year and weekday to day of week
  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
    return (Integer.parseInt(rule?.pattern?.week) == date.get(ChronoField.ALIGNED_WEEK_OF_YEAR) &&
            rule?.pattern?.weekday?.value?.toUpperCase() == date.getDayOfWeek().toString())
  }
}
