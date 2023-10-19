package org.olf.label.labelFormat

import grails.gorm.MultiTenant

import org.olf.label.LabelRule

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class LabelFormatChronologyYear extends LabelFormat implements MultiTenant<LabelFormatChronologyYear> {

  @CategoryId(value="Global.YearFormat", defaultInternal=true)
  @Defaults(['Full', 'Slice'])
  RefdataValue yearFormat

  static mapping = {
        yearFormat column: 'lfcy_year_format_fk'
  }

  static constraints = {
        yearFormat nullable: false
  }

  public static Map handleFormat(LabelRule rule, LocalDate date) {
    Map<String, String> getYearFormat = [
    	slice: 'yy',
     	full: 'yyyy',
   	]

	  DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern(getYearFormat.get(rule?.style?.format?.yearFormat?.value));

  	String year = date.format(yearFormatter);

  	return [year: year]
  }
}
