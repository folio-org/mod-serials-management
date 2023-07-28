package org.olf.omission.omissionPattern

import org.olf.omission.OmissionRule
import org.olf.internalPiece.InternalPiece

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
  public static boolean compareDate(OmissionRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    return (rule?.pattern?.day == date.getDayOfMonth())
  }
}
