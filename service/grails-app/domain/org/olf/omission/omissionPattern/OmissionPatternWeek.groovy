package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

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
}