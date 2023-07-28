package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import org.olf.omission.OmissionRule
import org.olf.internalPiece.InternalPiece

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternDayWeekMonth extends OmissionPattern implements MultiTenant<OmissionPatternDayWeekMonth> {

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  Integer week

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
    weekday column: 'opdwm_weekday_fk'
       week column: 'opdwm_week'
      month column: 'opdwm_month_fk'
  }

  static constraints = {
    weekday nullable: false
       week nullable: false
      month nullable: false

  }

  //Comparing month to month, week to week of month and weekday to day of week
  public static boolean compareDate(OmissionRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    return (rule?.pattern?.month?.value?.toUpperCase() == date.getMonth().toString() &&
            rule?.pattern?.week == date.get(ChronoField.ALIGNED_WEEK_OF_MONTH) &&
            rule?.pattern?.weekday?.value?.toUpperCase() == date.getDayOfWeek().toString())
  }
}
