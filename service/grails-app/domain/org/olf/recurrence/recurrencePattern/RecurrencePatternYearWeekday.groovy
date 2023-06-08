package org.olf.recurrence.recurrencePattern

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class RecurrencePatternYearWeekday extends RecurrencePattern implements MultiTenant<RecurrencePatternYearWeekday> {
  Integer week // Validated between 1-52 and -1

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
       week column: 'rpywd_week'
    weekday column: 'rpywd_weekday_fk'
  }

  static constraints = {
       week nullable: false, validator: { Integer val, RecurrencePattern obj, errors -> 
          if(!(val >= 1 && val <= 52)){
              errors.rejectValue('week', 'recurrence.pattern.value.not.in.range', ['Week', 1, 52] as Object[], 'Invalid week')
          }
         }
    weekday nullable: false
  }

  // Comparison for recurrence pattern year_weekday
  // Comapares pattern.week and weekday against week of year and day of week
  public static boolean compareDate(Map ruleset, LocalDate date, Integer index){
    return (ruleset?.recurrence?.rules[index]?.pattern?.week == date.get(ChronoField.ALIGNED_WEEK_OF_YEAR) &&
            ruleset?.recurrence?.rules[index]?.pattern?.weekday?.toUpperCase() == date.getDayOfWeek().toString())
  }
}
