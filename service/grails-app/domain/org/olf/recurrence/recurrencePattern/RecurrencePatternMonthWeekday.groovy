package org.olf.recurrence.recurrencePattern

import org.olf.recurrence.RecurrenceRule

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.temporal.ChronoField;

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue


public class RecurrencePatternMonthWeekday extends RecurrencePattern implements MultiTenant<RecurrencePatternMonthWeekday> {
  Integer week // Validated between 1-4 and -1

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
      week column: 'rpmwd_week'
    weekday column: 'rpmwd_weekday_fk'
  }

  static constraints = {
       week nullable: false, validator: { Integer val, RecurrencePattern obj, errors -> 
          if(!(val >= 1 && val <= 4)){
              errors.rejectValue('week', 'recurrence.pattern.value.not.in.range', ['Week', 1, 4] as Object[], 'Invalid week')
          }
         }
    weekday nullable: false
  }

  // Comparison for recurrence pattern type month_weekday
  // Checks to see if pattern.weekday and month equals dates weekday and week of month
  public static boolean compareDate(RecurrenceRule rule, LocalDate date){
    return (rule?.pattern?.week == date.get(ChronoField.ALIGNED_WEEK_OF_MONTH) &&
            rule?.pattern?.weekday?.value?.toUpperCase() == date.getDayOfWeek().toString())
  }
}
