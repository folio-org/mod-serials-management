package org.olf.combination.combinationPattern

import org.olf.combination.CombinationRule
import org.olf.internalPiece.InternalPiece

import grails.gorm.MultiTenant

import java.time.Month
import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

// TODO DECIDE IF WE'RE DOING THIS
public class CombinationPatternMonth extends CombinationPattern implements MultiTenant<CombinationPatternMonth> {

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
    month column: 'cpm_month_fk'
  }

  static constraints = {
    month nullable: false
  }

  public static boolean compareDate(CombinationRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
      return date?.get(ChronoField.MONTH_OF_YEAR) == Month.valueOf(rule.pattern.month.value.toUpperCase()).getValue() 
  }
}
