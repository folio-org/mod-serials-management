package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionPatternDaysInMonth extends OmissionPattern implements MultiTenant<OmissionPatternDaysInMonth> {
  Integer day

  @CategoryId(value="Global.Month", defaultInternal=true)
  @Defaults(['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
  RefdataValue month

  static mapping = {
      day column: 'opdim_day'
    month column: 'opdim_month_fk'
  }

  static constraints = {
      day nullable: false, validator: { Integer val, OmissionPattern obj, errors -> 
          if(!(val >= 1 && val <= 31)){
            // TODO Change error translation to a general key
              errors.rejectValue('day', 'recurrence.pattern.value.not.in.range', ['Day', 1, 31] as Object[], 'Invalid day')
          }
         }
    month nullable: false
  }
}
