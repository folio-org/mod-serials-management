package org.olf.recurrence.recurrencePattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class RecurrencePatternYearWeekday extends RecurrencePattern implements MultiTenant<RecurrencePatternYearWeekday> {
  Integer week // Validated between 1-52 and -1

  @CategoryId(value="RecurrencePattern.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
       week column: 'rpywd_week'
    weekday column: 'rpywd_weekday_fk'
  }

  static constraints = {
       week nullable: true
    weekday nullable: true
  }
}
