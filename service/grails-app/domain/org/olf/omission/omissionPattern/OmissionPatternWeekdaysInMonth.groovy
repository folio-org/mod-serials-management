package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternWeekdaysInMonth extends OmissionPattern implements MultiTenant<OmissionPatternWeekdaysInMonth> {

  Set<OmissionPatternWeekdayValues> weekdays

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static hasMany = [
    weekdays: OmissionPatternWeekdayValues
  ]

  static mappedBy = [
    weekdays: 'owner'
  ]

  static mapping = {
        month column: 'opwim_month_fk'
    weekdays cascade: 'all-delete-orphan'

  }

  static constraints = {
    weekdays nullable: false
    month nullable: false
  }
}
