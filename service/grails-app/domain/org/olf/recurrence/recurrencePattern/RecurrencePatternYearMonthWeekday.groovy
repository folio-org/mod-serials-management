package org.olf.recurrence.recurrencePattern

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.temporal.ChronoField;

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue


public class RecurrencePatternYearMonthWeekday extends RecurrencePattern implements MultiTenant<RecurrencePatternYearMonthWeekday> {
  Integer week // Validated between 1-4 and -1

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
       week column: 'rpymwd_week'
    weekday column: 'rpymwd_weekday_fk'
      month column: 'rpymwd_month_fk'
  }

  static constraints = {
       week nullable: false, validator: { Integer val, RecurrencePattern obj, errors -> 
          if(!(val >= 1 && val <= 4)){
              errors.rejectValue('week', 'recurrence.pattern.value.not.in.range', ['Week', 1, 4] as Object[], 'Invalid week')
          }
         }
    weekday nullable: false
      month nullable: false
  }

  public static boolean compareDate(Map ruleset, LocalDate date, Integer index){
    return (ruleset?.recurrence?.rules[index]?.pattern?.month?.value?.toUpperCase() == date.getMonth().toString() &&
            Integer.parseInt(ruleset?.recurrence?.rules[index]?.pattern?.week) == date.get(ChronoField.ALIGNED_WEEK_OF_MONTH) &&
            ruleset?.recurrence?.rules[index]?.pattern?.weekday?.value?.toUpperCase() == date.getDayOfWeek().toString())
  }
}
