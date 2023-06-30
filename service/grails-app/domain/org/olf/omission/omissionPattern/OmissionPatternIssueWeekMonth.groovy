package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.temporal.ChronoField

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternIssueWeekMonth extends OmissionPattern implements MultiTenant<OmissionPatternIssueWeekMonth> {

  Integer issue

  Integer week

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
    issue column: 'opiwm_issue'
     week column: 'opiwm_week'
    month column: 'opiwm_month_fk'
  }

  static constraints = {
    issue nullable: false
     week nullable: false
    month nullable: false
  }

  // Initially group all issues that fall within a specific week of month, month and specific year then compare the index with issue field value
  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
    ArrayList<String> weekMonthGroup = dates.findAll(x -> 
      x.date.get(ChronoField.ALIGNED_WEEK_OF_MONTH) == date.get(ChronoField.ALIGNED_WEEK_OF_MONTH) && 
      x.date.getMonth().toString() == date.getMonth().toString() && 
      x.date.get(ChronoField.YEAR) == date.get(ChronoField.YEAR)
    )
    return weekMonthGroup.get(Integer.parseInt(rule?.pattern?.issue) - 1)?.date == date &&
           weekMonthGroup.get(Integer.parseInt(rule?.pattern?.issue) - 1)?.date?.get(ChronoField.ALIGNED_WEEK_OF_MONTH) == Integer.parseInt(rule?.pattern?.week) &&
           weekMonthGroup.get(Integer.parseInt(rule?.pattern?.issue) - 1)?.date?.getMonth().toString() == rule?.pattern?.month?.value?.toUpperCase()
  }
}
