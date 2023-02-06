package org.olf.recurrence.recurrencePattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class RecurrencePatternWeek extends RecurrencePattern implements MultiTenant<RecurrencePatternWeek> {

  @CategoryId(value="RecurrencePattern.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
    weekday column: 'rpw_weekday_fk'
  }

  static constraints = {
    weekday nullable: true
  }
}
