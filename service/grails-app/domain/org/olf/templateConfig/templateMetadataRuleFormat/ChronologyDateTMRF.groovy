package org.olf.templateConfig.templateMetadataRuleFormat

import grails.gorm.MultiTenant

import org.olf.label.LabelRule

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyDateTMRF extends TemplateMetadataRuleFormat implements MultiTenant<ChronologyDateTMRF> {

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
  
	private static String getDayOfMonthSuffix(final int n) {
    if (n >= 11 && n <= 13) {
        return "th";
    }
    switch (n % 10) {
        case 1:  return "st";
        case 2:  return "nd";
        case 3:  return "rd";
        default: return "th";
    }
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

		Map<String, String> getWeekdayFormat = [
    	slice_lower: 'EE',
			slice_upper: 'EE',
     	full_upper: 'EEEE',
      full_lower: 'EEEE',
		]

		String weekday = date.format(DateTimeFormatter.ofPattern(getWeekdayFormat.get(rule?.style?.format?.weekdayFormat?.value)))
		if(rule?.style?.format?.weekdayFormat?.value.endsWith('upper')){
			weekday = weekday.toUpperCase()
		}

		String monthDay = date.format(DateTimeFormatter.ofPattern('d'))
		if(rule?.style?.format?.monthDayFormat?.value == 'ordinal'){
			monthDay = monthDay + getDayOfMonthSuffix(Integer.parseInt(monthDay))
		}

    String month = date.format(DateTimeFormatter.ofPattern(getMonthFormat.get(rule?.style?.format?.monthFormat?.value)));
  	String year = date.format(DateTimeFormatter.ofPattern(getYearFormat.get(rule?.style?.format?.yearFormat?.value)));

  	return [weekday: weekday, monthDay: monthDay, month: month, year: year]
  }  
}
