package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternIssue extends OmissionPattern implements MultiTenant<OmissionPatternIssue> {

  Integer issue

  static mapping = {
    issue column: 'opi_issue'
  }

  static constraints = {
    issue nullable: false
  }

  // Comparing the issue value to the index value of the dates array (+1 to since arrays initialise at 0)
  public static boolean compareDate(Map rule, LocalDate date, Integer index, ArrayList<String> dates){
    return index + 1 == Integer.parseInt(rule?.pattern?.issue)
  }
}
