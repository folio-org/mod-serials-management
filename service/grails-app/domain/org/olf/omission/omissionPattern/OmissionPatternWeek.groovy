package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import org.olf.omission.OmissionRule
import org.olf.internalPiece.InternalPiece

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
  public static boolean compareDate(OmissionRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    if(rule?.pattern?.isRange){
      return date?.get(ChronoField.ALIGNED_WEEK_OF_YEAR) >= rule.pattern.weekFrom &&
             date?.get(ChronoField.ALIGNED_WEEK_OF_YEAR) <= rule.pattern.weekTo
    }else{
      return date?.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == rule.pattern.weekFrom
    }
  }
}
