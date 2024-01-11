package org.olf.templateConfig.templateMetadataRuleFormat

import grails.gorm.MultiTenant

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRule
import org.olf.internalPiece.templateMetadata.ChronologyTemplateMetadata

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
    weekdayFormat column: 'cdtmrf_weekday_format_fk'
    monthDayFormat column: 'cdtmrf_month_day_format_fk'
    monthFormat column: 'cdtmrf_month_format_fk'
    yearFormat column: 'cdtmrf_year_format_fk'
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

    static Map<String, String> yearFormatTransform = [
    	slice: 'yy',
     	full: 'yyyy',
   	]

    static Map<String, String> monthFormatTransform = [
    	slice: 'MMM',
     	full: 'MMMM',
      number: 'MM'
		]

		static Map<String, String> weekdayFormatTransform = [
    	slice_lower: 'EE',
			slice_upper: 'EE',
     	full_upper: 'EEEE',
      full_lower: 'EEEE',
		]
   
  public static ChronologyTemplateMetadata handleFormat(TemplateMetadataRule rule, LocalDate date, int index) {
    ChronologyDateTMRF tmrf = rule?.ruleType?.ruleFormat
    // TODO Dont handle if not a chronology rule
		String weekday = date.format(DateTimeFormatter.ofPattern(weekdayFormatTransform.get(tmrf?.weekdayFormat?.value)))
		if(tmrf?.weekdayFormat?.value.endsWith('upper')){
			weekday = weekday.toUpperCase()
		}

		String monthDay = date.format(DateTimeFormatter.ofPattern('d'))
		if(tmrf?.monthDayFormat?.value == 'ordinal'){
			monthDay = monthDay + getDayOfMonthSuffix(Integer.parseInt(monthDay))
		}

    String month = date.format(DateTimeFormatter.ofPattern(monthFormatTransform.get(tmrf?.monthFormat?.value)));
  	String year = date.format(DateTimeFormatter.ofPattern(yearFormatTransform.get(tmrf?.yearFormat?.value)));

  	return new ChronologyTemplateMetadata([weekday: weekday, monthDay: monthDay, month: month, year: year, templateMetadataRule: rule])
  }  
}
