package org.olf.label.labelFormat

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class LabelFormatChronologyDate extends LabelFormat implements MultiTenant<LabelFormatChronologyDate> {

  @CategoryId(value="Global.WeekdayFormat", defaultInternal=true)
  @Defaults(['Slice Lower', 'Slice Upper', 'Full Lower', 'Full Upper'])
  RefdataValue weekdayFormat

  @CategoryId(value="Global.MonthDayFormat", defaultInternal=true)
  @Defaults(['Ordinal', 'Number'])
  RefdataValue monthDayFormat

  @CategoryId(value="Global.MonthFormat", defaultInternal=true)
  @Defaults(['Full', 'Slice', 'Number'])
  RefdataValue monthFormat

  @CategoryId(value="Global.YearFormat", defaultInternal=true)
  @Defaults(['Full', 'Slice'])
  RefdataValue yearFormat



  static mapping = {
     weekdayFormat column: 'lfcd_weekday_format_fk'
    monthDayFormat column: 'lfcd_month_day_format_fk'
       monthFormat column: 'lfcd_month_format_fk'
        yearFormat column: 'lfcd_year_format_fk'
  }

  static constraints = {
     weekdayFormat nullable: false
    monthDayFormat nullable: false
       monthFormat nullable: false
        yearFormat nullable: false
  }
}
