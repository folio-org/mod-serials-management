package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import org.olf.omission.OmissionRule
import org.olf.internalPiece.InternalPiece

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternWeekMonth extends OmissionPattern implements MultiTenant<OmissionPatternWeekMonth> {

  Integer week

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
     week column: 'opwm_week'
    month column: 'opwm_month_fk'
  }

  static constraints = {
     week nullable: false
    month nullable: false
  }

  // Compare week to week of month and month to month of year
  public static boolean compareDate(OmissionRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    return (rule?.pattern?.month?.value?.toUpperCase() == date.getMonth().toString() &&
            rule?.pattern?.week == date.get(ChronoField.ALIGNED_WEEK_OF_MONTH))
    }
}
