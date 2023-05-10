package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternIssueWeek extends CombinationPattern implements MultiTenant<CombinationPatternIssueWeek> {

  Integer issue

  Integer week

  static mapping = {
    issue column: 'cpiw_issue'
     week column: 'cpiw_week'
  }

  static constraints = {
    issue nullable: false
     week nullable: false
  }
}
