package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.temporal.ChronoField

public class OmissionPatternWeek extends OmissionPattern implements MultiTenant<OmissionPatternWeek> {

  Integer weekFrom
  Integer weekTo
  boolean isRange = false

  static mapping = {
    weekFrom column: 'opw_week_from'
      weekTo column: 'opw_week_to'
     isRange column: 'opw_is_range'
  }

  static constraints = {
    weekFrom nullable: false
      weekTo nullable: true
     isRange nullable: false
  }

  // If fields are a range, compare to see if numerical value of week falls within these
  // If not, compare week to week of year
  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
    if(rule?.pattern?.isRange){
      return date?.get(ChronoField.ALIGNED_WEEK_OF_YEAR) >= Integer.parseInt(rule.pattern.weekFrom) &&
             date?.get(ChronoField.ALIGNED_WEEK_OF_YEAR) <= Integer.parseInt(rule.pattern.weekTo)
    }else{
      return date?.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == Integer.parseInt(rule.pattern.weekFrom) 
    }
  }
}
