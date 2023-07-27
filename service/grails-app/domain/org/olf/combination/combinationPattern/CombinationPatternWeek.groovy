package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternWeek extends CombinationPattern implements MultiTenant<CombinationPatternWeek> {

  Integer week

  static mapping = {
    week column: 'cpw_week'
  }

  static constraints = {
    week nullable: false
  }

  // If fields are a range, compare to see if numerical value of week falls within these
  // If not, compare week to week of year
  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
      return date?.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == Integer.parseInt(rule.pattern.week) 
  }
}
