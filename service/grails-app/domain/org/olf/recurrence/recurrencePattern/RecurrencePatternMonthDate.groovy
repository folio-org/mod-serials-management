package org.olf

import grails.gorm.MultiTenant

public class RecurrencePatternMonthDate extends RecurrencePattern implements MultiTenant<RecurrencePatternMonthDate> {
  Integer day // Validated 1-28 and -1 // OR special refdata
  // Fallback stuff?

  static mapping = {
    day column: 'rep_day'
  }

  static constraints = {
    day nullable: true
  }
}
