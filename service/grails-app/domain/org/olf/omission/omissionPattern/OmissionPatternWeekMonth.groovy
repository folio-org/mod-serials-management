package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternWeekMonth extends OmissionPattern implements MultiTenant<OmissionPatternWeekMonth> {

  Integer week

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
     week column: 'opwm_week'
    month column: 'opwm_month_fk'
  }

  static constraints = {
     week nullable: false
    month nullable: false
  }
}
