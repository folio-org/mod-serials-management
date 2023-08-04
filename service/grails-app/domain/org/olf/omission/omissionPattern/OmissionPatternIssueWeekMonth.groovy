package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import org.olf.omission.OmissionRule
import org.olf.internalPiece.*

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
  public static boolean compareDate(OmissionRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    ArrayList<InternalPiece> weekMonthGroup = InternalPiece.conditionalGroupRecurrencePieces(internalPieces){ip ->

      return ip.date.getMonth().toString() == rule.pattern.month.value.toUpperCase() &&
      ip.date.get(ChronoField.ALIGNED_WEEK_OF_MONTH) == rule.pattern.week && 
      ip.date.get(ChronoField.YEAR) == date.get(ChronoField.YEAR)
    }
    if(weekMonthGroup.size() < rule?.pattern?.issue){
      return false
    }
    return weekMonthGroup.get(rule?.pattern?.issue - 1)?.date == date &&
           weekMonthGroup.get(rule?.pattern?.issue - 1)?.date?.get(ChronoField.ALIGNED_WEEK_OF_MONTH) == rule?.pattern?.week &&
           weekMonthGroup.get(rule?.pattern?.issue - 1)?.date?.getMonth().toString() == rule?.pattern?.month?.value?.toUpperCase()
  }
}
