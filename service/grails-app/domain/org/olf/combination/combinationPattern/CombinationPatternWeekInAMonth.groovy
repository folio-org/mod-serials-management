package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternWeekInAMonth extends CombinationPattern implements MultiTenant<CombinationPatternWeekInAMonth> {

  Integer week

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
      month column: 'cpwioam_month_fk'
       week column: 'cpwioam_week'
  }

  static constraints = {
      month nullable: false
       week nullable: false
  }
}
