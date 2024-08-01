package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRule
import org.olf.internalPiece.templateMetadata.EnumerationUCTMT
import org.olf.internalPiece.templateMetadata.EnumerationLevelUCTMT

import java.time.LocalDate

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import com.github.fracpete.romannumerals4j.RomanNumeralFormat;

public class EnumerationNumericTMRF extends TemplateMetadataRuleFormat implements MultiTenant<EnumerationNumericTMRF> {
  Set<EnumerationNumericLevelTMRF> levels
  
  static hasMany = [
    levels: EnumerationNumericLevelTMRF,
  ]

  static mapping = {
    levels cascade: 'all-delete-orphan', sort: 'index', order: 'asc'
  }
  
  static constraints = {
    levels nullable: false
  }

  private static String getOrdinalSuffix(final int n) {
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

  public static EnumerationUCTMT handleFormat (TemplateMetadataRule rule, LocalDate date, int index, EnumerationUCTMT startingValues){
    RomanNumeralFormat rnf = new RomanNumeralFormat();

    ArrayList<EnumerationNumericLevelTMRF> enltmrfArray = rule?.ruleType?.ruleFormat?.levels?.sort { it?.index }
    ArrayList<EnumerationLevelUCTMT> svArray = startingValues?.levels?.sort { it?.index }
    ArrayList<EnumerationLevelUCTMT> result = []

    Integer adjustedIndex = 0
    Integer divisor = 1

    for(int i=enltmrfArray?.size()-1; i>=0; i--){
      if(svArray?.getAt(i)?.value !== null){
        adjustedIndex = adjustedIndex + ((svArray.getAt(i)?.value as Integer - 1)*divisor)
      }

      Integer value = 0
      for(int j=0; j<=index+adjustedIndex; j++){
        if(j % divisor == 0){
          value++
        }
      }

      if(enltmrfArray[i]?.sequence?.value == 'reset'){
        if(value%enltmrfArray[i]?.units == 0){
          value = enltmrfArray[i]?.units
        }else{
          value = value%enltmrfArray[i]?.units
        }
      }

      String stringValue = value
      if(enltmrfArray[i]?.format?.value == 'ordinal'){
        stringValue = value + getOrdinalSuffix(value)
      }

      if(enltmrfArray[i]?.format?.value == 'roman'){
        stringValue = rnf.format(value)
      }

      result.add([
        value: stringValue,
        rawValue: value,
        valueFormat: enltmrfArray[i]?.format,
        index: i
      ])
      
      divisor = enltmrfArray[i]?.units*divisor
    }
    return new EnumerationUCTMT([levels: result.reverse()])
  }
}
