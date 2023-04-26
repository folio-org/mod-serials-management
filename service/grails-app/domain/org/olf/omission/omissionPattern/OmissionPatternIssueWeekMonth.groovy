package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

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
}
