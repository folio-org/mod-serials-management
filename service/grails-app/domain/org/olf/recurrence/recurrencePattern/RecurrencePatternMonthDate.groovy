package org.olf.recurrence.recurrencePattern

import grails.gorm.MultiTenant

public class RecurrencePatternMonthDate extends RecurrencePattern implements MultiTenant<RecurrencePatternMonthDate> {
  Integer day // Validated 1-28 and -1 // OR special refdata
  // Fallback stuff?

  static mapping = {
    day column: 'rpmd_day'
  }

  static constraints = {
    day nullable: false, validator: { Integer val, RecurrencePattern obj, errors -> 
          if(!(val >= 1 && val <= 31)){
              errors.rejectValue('day', 'recurrence.pattern.value.not.in.range', ['Day', 1, 31] as Object[], 'Invalid day')
          }
         }
  }
}
