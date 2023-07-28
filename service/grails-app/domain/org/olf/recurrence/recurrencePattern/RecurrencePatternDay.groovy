package org.olf.recurrence.recurrencePattern

import org.olf.recurrence.RecurrenceRule

import java.time.LocalDate

import grails.gorm.MultiTenant

public class RecurrencePatternDay extends RecurrencePattern implements MultiTenant<RecurrencePatternDay> {
  
  // Comparison for recurrence pattern type month_date
  // Currently only works for daily recurrence, adds date for each iteration
  public static boolean compareDate(RecurrenceRule rule, LocalDate date){
    return true
  }
}
