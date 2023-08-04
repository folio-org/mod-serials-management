package org.olf.recurrence.recurrencePattern

import org.olf.recurrence.RecurrenceRule

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class RecurrencePatternWeek extends RecurrencePattern implements MultiTenant<RecurrencePatternWeek> {

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
    weekday column: 'rpw_weekday_fk'
  }

  static constraints = {
    weekday nullable: false
  }

  // Comparison for recurrence pattern type week
  // Checks to see if pattern.weekday equals dates weekday
  public static boolean compareDate(RecurrenceRule rule, LocalDate date){
    return (rule?.pattern?.weekday?.value?.toUpperCase() == date.getDayOfWeek().toString())
  }
}
