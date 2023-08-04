package org.olf.combination.combinationPattern

import org.olf.combination.CombinationRule
import org.olf.internalPiece.InternalPiece

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

// TODO DECIDE IF WE'RE DOING THIS
public class CombinationPatternDayMonth extends CombinationPattern implements MultiTenant<CombinationPatternDayMonth> {

  Integer day

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
      day column: 'cpdm_day'
    month column: 'cpdm_month_fk'
  }

  static constraints = {
      day nullable: false
    month nullable: false

  }

  // Comparing day field to day of month and month field to month
  public static boolean compareDate(CombinationRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    return (rule?.pattern?.day == date.getDayOfMonth() &&
            rule?.pattern?.month?.value?.toUpperCase() == date.getMonth().toString())
  }
}
