package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.ChronologyTemplateMetadataRule
import org.olf.internalPiece.templateMetadata.ChronologyUCTMT

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import java.util.Locale

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyMonthTMRF extends ChronologyTemplateMetadataRuleFormat implements MultiTenant<ChronologyMonthTMRF> {
  
  @CategoryId(value="Global.MonthFormat", defaultInternal=true)
  @Defaults(['Full', 'Slice', 'Number'])
  RefdataValue monthFormat

  @CategoryId(value="Global.YearFormat", defaultInternal=true)
  @Defaults(['Full', 'Slice'])
  RefdataValue yearFormat

  static mapping = {
    monthFormat column: 'cmtmrf_month_format_fk'
    yearFormat column: 'cmtmrf_year_format_fk'
  }

  static constraints = {
    monthFormat nullable: false
    yearFormat nullable: false
  }

  public static ChronologyUCTMT handleFormat(ChronologyTemplateMetadataRule rule, LocalDate date, int index) {
    Locale locale = new Locale(rule?.ruleLocale)
    ChronologyMonthTMRF tmrf = rule?.ruleFormat
    Map<String, String> getYearFormat = [
    	slice: 'yy',
     	full: 'yyyy',
   	]

    Map<String, String> getMonthFormat = [
    	slice: 'MMM',
     	full: 'MMMM',
      number: 'MM'
   	]
    
 	  DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern(getMonthFormat.get(tmrf?.monthFormat?.value), locale);
	  DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern(getYearFormat.get(tmrf?.yearFormat?.value), locale);

    String month = date.format(monthFormatter);
  	String year = date.format(yearFormatter);

  	return new ChronologyUCTMT([month: month, year: year, templateMetadataRule: rule])
  }  
}
