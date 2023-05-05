package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternDay extends CombinationPattern implements MultiTenant<CombinationPatternDay> {

  Integer day

  static mapping = {
    day column: 'cpd_day'
  }

  static constraints = {
    day nullable: false
  }
}
