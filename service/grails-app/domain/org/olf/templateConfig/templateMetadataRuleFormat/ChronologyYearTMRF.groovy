package org.olf.templateConfig.templateMetadataRuleFormat

import grails.gorm.MultiTenant

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRule
import org.olf.internalPiece.templateMetadata.ChronologyTemplateMetadata

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyYearTMRF extends TemplateMetadataRuleFormat implements MultiTenant<ChronologyYearTMRF> {

  @CategoryId(value="Global.YearFormat", defaultInternal=true)
  @Defaults(['Full', 'Slice'])
  RefdataValue yearFormat

  static mapping = {
    yearFormat column: 'cytmrf_year_format_fk'
  }

  static constraints = {
    yearFormat nullable: false
  }

  public static ChronologyTemplateMetadata handleFormat(TemplateMetadataRule rule, LocalDate date, int index) {
    println(rule)
    Locale locale = new Locale(rule?.ruleType?.ruleLocale)
		ChronologyYearTMRF tmrf = rule?.ruleType?.ruleFormat
    Map<String, String> getYearFormat = [
    	slice: 'yy',
     	full: 'yyyy',
   	]

	  DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern(getYearFormat.get(tmrf?.yearFormat?.value), locale);

  	String year = date.format(yearFormatter);

  	return new ChronologyTemplateMetadata([year: year, templateMetadataRule: rule])
  }
}
