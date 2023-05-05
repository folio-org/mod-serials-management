package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternDayWeekMonth extends CombinationPattern implements MultiTenant<CombinationPatternDayWeekMonth> {

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  Integer week

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
    weekday column: 'cpdwm_weekday_fk'
       week column: 'cpdwm_week'
      month column: 'cpdwm_month_fk'
  }

  static constraints = {
    weekday nullable: false
       week nullable: false
      month nullable: false

  }
}
