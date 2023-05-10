package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternIssueMonth extends CombinationPattern implements MultiTenant<CombinationPatternIssueMonth> {

  Integer issue

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
    issue column: 'cpim_issue'
    month column: 'cpim_month_fk'
  }

  static constraints = {
    issue nullable: false
    month nullable: false
  }
}
