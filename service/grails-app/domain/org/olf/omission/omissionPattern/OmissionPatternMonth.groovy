package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import java.time.Month
import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternMonth extends OmissionPattern implements MultiTenant<OmissionPatternMonth> {

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue monthFrom

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue monthTo

  boolean isRange = false

  static mapping = {
    monthFrom column: 'opm_month_from_fk'
      monthTo column: 'opm_month_to_fk'
      isRange column: 'opm_is_range'
  }

  static constraints = {
    monthFrom nullable: false
      monthTo nullable: true
      isRange nullable: false
  }

  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
    if(rule?.pattern?.isRange){
      return date?.get(ChronoField.MONTH_OF_YEAR) >= Month.valueOf(rule.pattern.monthFrom.value.toUpperCase()).getValue() &&
             date?.get(ChronoField.MONTH_OF_YEAR) <= Month.valueOf(rule.pattern.monthTo.value.toUpperCase()).getValue()
    }else{
      return date?.get(ChronoField.MONTH_OF_YEAR) == Month.valueOf(rule.pattern.monthFrom.value.toUpperCase()).getValue() 
    }
  }
}
