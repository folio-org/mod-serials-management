package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternWeekdayInWeekOfAMonth extends CombinationPattern implements MultiTenant<CombinationPatternWeekdayInWeekOfAMonth> {

  Integer week

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
      month column: 'cpwdiwoam_month_fk'
    weekday column: 'cpwdiwoam_weekday_fk'
       week column: 'cpwdiwoam_week'
  }

  static constraints = {
      month nullable: false
    weekday nullable: false
       week nullable: false
  }
}
