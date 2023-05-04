package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternDayMonth extends CombinationPattern implements MultiTenant<CombinationPatternDayMonth> {

  Integer day

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
      day column: 'cpdm_day'
    month column: 'cpdm_month_fk'
  }

  static constraints = {
      day nullable: false
    month nullable: false

  }
}
