package org.olf

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue


public class RecurrencePatternMonthWeekday extends RecurrencePattern implements MultiTenant<RecurrencePatternMonthWeekday> {
  Integer week // Validated between 1-4 and -1

  @CategoryId(value="RecurrencePattern.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday
}
