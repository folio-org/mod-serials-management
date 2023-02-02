package org.olf.recurrence.recurrencePattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class RecurrencePatternYearDate extends RecurrencePattern implements MultiTenant<RecurrencePatternYearDate> {
  Integer day // Validated between 1-31 and -1 against month

  @CategoryId(value="RecurrencePattern.Month", defaultInternal=true)
  @Defaults(['January', 'Febuary', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
      day column: 'rpyd_day'
    month column: 'rpyd_month_fk'
  }

  static constraints = {
      day nullable: true
    month nullable: true
  }
}
