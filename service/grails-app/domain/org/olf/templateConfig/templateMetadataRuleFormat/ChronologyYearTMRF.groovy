package org.olf.templateConfig.templateMetadataRuleFormat

import grails.gorm.MultiTenant

import org.olf.templateConfig.templateMetadataRule.ChronologyTemplateMetadataRule
import org.olf.internalPiece.templateMetadata.ChronologyUCTMT

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyYearTMRF extends ChronologyTemplateMetadataRuleFormat implements MultiTenant<ChronologyYearTMRF> {

  @CategoryId(value="Global.YearFormat", defaultInternal=true)
  @Defaults(['Full', 'Slice'])
  RefdataValue yearFormat

  static mapping = {
    yearFormat column: 'cytmrf_year_format_fk'
  }

  static constraints = {
    yearFormat nullable: false
  }

  public static ChronologyUCTMT handleFormat(ChronologyTemplateMetadataRule rule, LocalDate date, int index) {
    Locale locale = new Locale(rule?.ruleLocale)
		ChronologyYearTMRF tmrf = rule?.ruleFormat
    Map<String, String> getYearFormat = [
    	slice: 'yy',
     	full: 'yyyy',
   	]

	  DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern(getYearFormat.get(tmrf?.yearFormat?.value), locale);

  	String year = date.format(yearFormatter);

  	return new ChronologyUCTMT([year: year, templateMetadataRule: rule])
  }
}
