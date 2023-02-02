package org.olf.recurrence.recurrencePattern

import grails.gorm.MultiTenant

public class RecurrencePatternMonthDate extends RecurrencePattern implements MultiTenant<RecurrencePatternMonthDate> {
  Integer day // Validated 1-28 and -1 // OR special refdata
  // Fallback stuff?

  static mapping = {
    day column: 'rpmd_day'
  }

  static constraints = {
    day nullable: true
  }
}
