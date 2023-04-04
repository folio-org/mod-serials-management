package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternMonths extends OmissionPattern implements MultiTenant<OmissionPatternMonths> {

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue monthFrom

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue monthTo

  boolean isRange = false

  static mapping = {
    monthFrom column: 'opm_month_from_fk'
      monthTo column: 'opm_month_to_fk'
      isRange column: 'opm_is_range'
  }

  static constraints = {
    monthFrom nullable: false
      monthTo nullable: true
      isRange nullable: false
  }
}
