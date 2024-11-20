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

    // Array of EnumerationNumericLevels sorted by index
    ArrayList<EnumerationNumericLevelTMRF> enltmrfArray = rule?.ruleType?.ruleFormat?.levels?.sort { it?.index }
    // Array of starting values sorted by index
    ArrayList<EnumerationLevelUCTMT> svArray = startingValues?.levels?.sort { it?.index }
    ArrayList<EnumerationLevelUCTMT> result = []

    Boolean shouldIncrement = true
    // Iterate through the EnumerationNumericLevels starting at the lowest level
    for(int i=enltmrfArray?.size()-1; i>=0; i--){

      // Set value to previous pieces corresponding value
      Integer value = (svArray?.getAt(i)?.rawValue ?: svArray?.getAt(i)?.value) as Integer
      // Only calculate if we're past the first piece, otherwise use starting values
      if(index != 0){
        //If previous level has set flag to  true, increment
        //Always incremement on lowest level then set to false so higher levels should only ever increment with permission from lower levels
        if(shouldIncrement == true){
          if (value % enltmrfArray[i]?.units != 0) {
            shouldIncrement = false
          }
          value ++
        }
      }

      // If the number should be reset on every cycle, calculate its actual value
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
    }
    return new EnumerationUCTMT([levels: result.reverse()])
  }
}
