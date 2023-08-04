package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import org.olf.omission.OmissionRule
import org.olf.internalPiece.InternalPiece

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternDayMonth extends OmissionPattern implements MultiTenant<OmissionPatternDayMonth> {

  Integer day

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
      day column: 'opdm_day'
    month column: 'opdm_month_fk'
  }

  static constraints = {
      day nullable: false
    month nullable: false

  }

  // Comparing day field to day of month and month field to month
  public static boolean compareDate(OmissionRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    return (rule?.pattern?.day == date.getDayOfMonth() &&
            rule?.pattern?.month?.value?.toUpperCase() == date.getMonth().toString())
  }
}
