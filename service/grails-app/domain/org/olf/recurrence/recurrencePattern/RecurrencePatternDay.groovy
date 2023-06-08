package org.olf.recurrence.recurrencePattern

import java.time.LocalDate

import grails.gorm.MultiTenant

public class RecurrencePatternDay extends RecurrencePattern implements MultiTenant<RecurrencePatternDay> {
  
  // Comparison for recurrence pattern type month_date
  // Currently only works for daily recurrence, adds date for each iteration
  public static boolean compareDate(Map ruleset, LocalDate date, Integer index){
    return true
  }
}
