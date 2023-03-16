package org.olf.recurrence.recurrencePattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue


public class RecurrencePatternYearMonthWeekday extends RecurrencePattern implements MultiTenant<RecurrencePatternYearMonthWeekday> {
  Integer week // Validated between 1-4 and -1

  @CategoryId(value="RecurrencePattern.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  @CategoryId(value="RecurrencePattern.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
       week column: 'rpymwd_week'
    weekday column: 'rpymwd_weekday_fk'
      month column: 'rpymwd_month_fk'
  }

  static constraints = {
       week nullable: false
    weekday nullable: false
      month nullable: false
  }
}
