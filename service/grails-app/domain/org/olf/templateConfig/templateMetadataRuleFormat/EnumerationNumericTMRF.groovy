package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRule
import org.olf.internalPiece.templateMetadata.EnumerationTemplateMetadata
import org.olf.internalPiece.templateMetadata.EnumerationTemplateMetadataLevel

import java.time.LocalDate

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

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

  public static String intToRoman(int num){  
    Integer[] values = [1000,900,500,400,100,90,50,40,10,9,5,4,1];  
    String[] romanLetters = ["M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"];  
    String roman = '' 
    for(int i=0;i<values.length;i++){  
      while(num >= values[i]){  
        num = num - values[i];  
        roman += romanLetters[i];  
      }  
    } 

    return roman 
  }  

  public static EnumerationTemplateMetadata handleFormat (TemplateMetadataRule rule, LocalDate date, int index){
    EnumerationNumericTMRF entmrf = rule?.ruleType?.ruleFormat
    ArrayList<EnumerationTemplateMetadataLevel> result = []
    Integer divisor = 1
    for(int i=entmrf?.levels?.size()-1; i>=0; i--){
      Integer value = 0
      for(int j=0; j<=index; j++){
        if(j % divisor == 0){
          value++
        }
      }
      if(entmrf?.levels[i]?.sequence?.value == 'reset'){
        if(value%entmrf?.levels[i]?.units == 0){
          value = entmrf?.levels[i]?.units
        }else{
          value = value%entmrf?.levels[i]?.units
        }
      }

      String stringValue = value
      if(entmrf?.levels[i]?.format?.value == 'ordinal'){
        stringValue = value + getOrdinalSuffix(value)
      }

      if(entmrf?.levels[i]?.format?.value == 'roman'){
        stringValue = intToRoman(value)
      }

      result.add([value: stringValue])

      divisor = entmrf?.levels[i]?.units*divisor
    }
    return new EnumerationTemplateMetadata([levels: result.reverse()])
  }
}
