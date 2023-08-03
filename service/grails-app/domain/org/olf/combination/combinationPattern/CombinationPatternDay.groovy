package org.olf.combination.combinationPattern

import org.olf.combination.CombinationRule
import org.olf.internalPiece.InternalPiece

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

// TODO DECIDE IF WE'RE DOING THIS
public class CombinationPatternDay extends CombinationPattern implements MultiTenant<CombinationPatternDay> {

  Integer day

  static mapping = {
    day column: 'cpd_day'
  }

  static constraints = {
    day nullable: false
  }

  // Comparing day field to dates "day of month"
  public static boolean compareDate(CombinationRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    return (rule?.pattern?.day == date.getDayOfMonth())
  }
}
