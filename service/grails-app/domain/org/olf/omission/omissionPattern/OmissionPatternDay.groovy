package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternDay extends OmissionPattern implements MultiTenant<OmissionPatternDay> {

  Integer day

  static mapping = {
    day column: 'opd_day'
  }

  static constraints = {
    day nullable: false
  }
}
