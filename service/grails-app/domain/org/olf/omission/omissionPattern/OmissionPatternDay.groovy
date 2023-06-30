package org.olf.omission.omissionPattern

import java.time.LocalDate

import grails.gorm.MultiTenant

public class OmissionPatternDay extends OmissionPattern implements MultiTenant<OmissionPatternDay> {

  Integer day

  static mapping = {
    day column: 'opd_day'
  }

  static constraints = {
    day nullable: false
  }

  // Comparing day field to dates "day of month"
  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
    return (Integer?.parseInt(rule?.pattern?.day) == date.getDayOfMonth())
  }
}
