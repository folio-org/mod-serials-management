package org.olf.label.labelFormat

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class LabelFormatChronologyMonth extends LabelFormat implements MultiTenant<LabelFormatChronologyMonth> {
  
  @CategoryId(value="Global.MonthFormat", defaultInternal=true)
  @Defaults(['Full', 'Slice', 'Number'])
  RefdataValue monthFormat

  @CategoryId(value="Global.YearFormat", defaultInternal=true)
  @Defaults(['Full', 'Slice'])
  RefdataValue yearFormat

  static mapping = {
       monthFormat column: 'lfcm_month_format_fk'
        yearFormat column: 'lfcm_year_format_fk'
  }

  static constraints = {
       monthFormat nullable: false
        yearFormat nullable: false
  }
}
