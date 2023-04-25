package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternIssueWeek extends OmissionPattern implements MultiTenant<OmissionPatternIssueWeek> {

  Integer issue

  Integer week

  static mapping = {
    issue column: 'opiw_issue'
     week column: 'opiw_week'
  }

  static constraints = {
    issue nullable: false
     week nullable: false
  }
}
