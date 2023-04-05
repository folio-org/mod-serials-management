package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternWeeksInEveryMonth extends OmissionPattern implements MultiTenant<OmissionPatternWeeksInEveryMonth> {

  Integer week

  static mapping = {
    week column: 'opwiem_week'

  }

  static constraints = {
    week nullable: false
  }
}
