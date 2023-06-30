package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternIssueMonth extends OmissionPattern implements MultiTenant<OmissionPatternIssueMonth> {

  Integer issue

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
    issue column: 'opim_issue'
    month column: 'opim_month_fk'
  }

  static constraints = {
    issue nullable: false
    month nullable: false
  }

  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
    ArrayList<String> monthGroup = dates.findAll(x -> x.date.getMonth().toString() == date.getMonth().toString() && x.date.get(ChronoField.YEAR) == date.get(ChronoField.YEAR))
    return monthGroup.get(Integer.parseInt(rule?.pattern?.issue) - 1)?.date == date &&
           monthGroup.get(Integer.parseInt(rule?.pattern?.issue) - 1)?.date?.getMonth()?.toString() == rule?.pattern?.month?.value?.toUpperCase()
  }
}
