package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternIssueWeekMonth extends CombinationPattern implements MultiTenant<CombinationPatternIssueWeekMonth> {

  Integer issue

  Integer week

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
    issue column: 'cpiwm_issue'
     week column: 'cpiwm_week'
    month column: 'cpiwm_month_fk'
  }

  static constraints = {
    issue nullable: false
     week nullable: false
    month nullable: false
  }
}
