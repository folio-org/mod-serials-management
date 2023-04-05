package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternWeekdaysInWeek extends OmissionPattern implements MultiTenant<OmissionPatternWeekdaysInWeek> {

  Integer week

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
    weekday column: 'opwdiw_weekday_fk'
       week column: 'opwdiw_week'
  }

  static constraints = {
    weekday nullable: false
       week nullable: false
  }
}
