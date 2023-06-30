package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternDayWeekday extends OmissionPattern implements MultiTenant<OmissionPatternDayWeekday> {

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

  static mapping = {
    weekday column: 'opdwd_weekday_fk'
  }

  static constraints = {
    weekday nullable: false
  }

  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
    return (rule?.pattern?.weekday?.value?.toUpperCase() == date.getDayOfWeek().toString())
  }
}
