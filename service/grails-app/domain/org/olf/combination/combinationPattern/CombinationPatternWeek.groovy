package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternWeek extends CombinationPattern implements MultiTenant<CombinationPatternWeek> {

  Integer week

  static mapping = {
    week column: 'cpw_week'
  }

  static constraints = {
    week nullable: false
  }
}
