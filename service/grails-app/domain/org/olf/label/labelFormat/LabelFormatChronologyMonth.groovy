package org.olf.label.labelFormat

import grails.gorm.MultiTenant

import org.olf.label.LabelRule

import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

  public static Map handleFormat(LabelRule rule, LocalDate date) {
    Map<String, String> getYearFormat = [
    	slice: 'yy',
     	full: 'yyyy',
   	]

    Map<String, String> getMonthFormat = [
    	slice: 'MMM',
     	full: 'MMMM',
      number: 'MM'
   	]
    
 	  DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern(getMonthFormat.get(rule?.style?.format?.monthFormat?.value));
	  DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern(getYearFormat.get(rule?.style?.format?.yearFormat?.value));

    String month = date.format(monthFormatter);
  	String year = date.format(yearFormatter);

  	return [month: month, year: year]
  }  
}
