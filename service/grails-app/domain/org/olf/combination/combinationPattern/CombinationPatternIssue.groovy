package org.olf.combination.combinationPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationPatternIssue extends CombinationPattern implements MultiTenant<CombinationPatternIssue> {

  Integer issue

  static mapping = {
    issue column: 'cpi_issue'
  }

  static constraints = {
    issue nullable: false
  }
}
