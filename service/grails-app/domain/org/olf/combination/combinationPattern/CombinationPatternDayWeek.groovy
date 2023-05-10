package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternDayWeek extends CombinationPattern implements MultiTenant<CombinationPatternDayWeek> {

  Integer week

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
      week column : 'cpdw_week'
    weekday column: 'cpdw_weekday_fk'
  }

  static constraints = {
       week nullable: false
    weekday nullable: false
  }
}
