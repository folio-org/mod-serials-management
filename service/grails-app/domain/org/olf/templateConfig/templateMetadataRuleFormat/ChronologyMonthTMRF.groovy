package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRule
import org.olf.internalPiece.templateMetadata.ChronologyTemplateMetadata

import grails.gorm.MultiTenant

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyMonthTMRF extends TemplateMetadataRuleFormat implements MultiTenant<ChronologyMonthTMRF> {
  
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

  public static ChronologyTemplateMetadata handleFormat(TemplateMetadataRule rule, LocalDate date, int index) {
    ChronologyMonthTMRF tmrf = rule?.ruleType?.ruleFormat
    Map<String, String> getYearFormat = [
    	slice: 'yy',
     	full: 'yyyy',
   	]

    Map<String, String> getMonthFormat = [
    	slice: 'MMM',
     	full: 'MMMM',
      number: 'MM'
   	]
    
 	  DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern(getMonthFormat.get(tmrf?.monthFormat?.value));
	  DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern(getYearFormat.get(tmrf?.yearFormat?.value));

    String month = date.format(monthFormatter);
  	String year = date.format(yearFormatter);

  	return new ChronologyTemplateMetadata([month: month, year: year, templateMetadataRule: rule])
  }  
}
