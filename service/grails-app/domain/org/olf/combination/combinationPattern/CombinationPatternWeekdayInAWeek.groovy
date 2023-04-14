package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternWeekdayInAWeek extends CombinationPattern implements MultiTenant<CombinationPatternWeekdayInAWeek> {

  Integer week

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
    weekday column: 'cpwdiaw_weekday_fk'
       week column: 'cpwdiaw_week'
  }

  static constraints = {
    weekday nullable: false
       week nullable: false
  }
}
