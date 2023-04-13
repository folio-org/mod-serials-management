package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternNthIssue extends CombinationPattern implements MultiTenant<CombinationPatternNthIssue> {

  Integer issue

  static mapping = {
    issue column: 'cpni_week'
  }

  static constraints = {
    issue nullable: false
  }
}
