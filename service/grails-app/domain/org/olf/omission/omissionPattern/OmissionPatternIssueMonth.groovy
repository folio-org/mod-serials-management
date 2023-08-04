package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import org.olf.omission.OmissionRule
import org.olf.internalPiece.*

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
  // Only considering internal recurrence pieces/omission pieces within a month
  // Initially group all issues that fall within a specific month and specific year then compare the index with issue field value
  public static boolean compareDate(OmissionRule rule, LocalDate date, ArrayList<InternalPiece> internalPieces){
    ArrayList<InternalPiece> monthGroup = InternalPiece.conditionalGroupRecurrencePieces(internalPieces){ip -> 
      return ip.date.getMonth().toString() == rule.pattern.month.value.toUpperCase() && 
      ip.date.get(ChronoField.YEAR) == date.get(ChronoField.YEAR)
    }
    if(monthGroup.size() < rule?.pattern?.issue){
      return false
    }
    return monthGroup.get(rule?.pattern?.issue - 1)?.date == date &&
           monthGroup.get(rule?.pattern?.issue - 1)?.date?.getMonth()?.toString() == rule?.pattern?.month?.value?.toUpperCase()
  }
}
