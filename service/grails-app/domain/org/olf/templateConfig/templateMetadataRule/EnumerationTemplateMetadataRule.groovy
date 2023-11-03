package org.olf.templateConfig.templateMetadataRule

import org.olf.label.LabelRule

import java.time.LocalDate

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationTemplateMetadataRule extends TemplateMetadataRuleType implements MultiTenant<EnumerationTemplateMetadataRule> {
  ArrayList<EnumerationTemplateMetadataRuleLevel> ruleLevels

  static mapping = {
    ruleLevels cascade: 'all-delete-orphan'
  }
  
  static constraints = {
    ruleLevels nullable: false
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

  public static ArrayList<Map> handleStyle (LabelRule rule, LocalDate date, int index){
    ArrayList<Map> result = []
    Integer divisor = 1
    for(int i=rule?.style?.levels?.size()-1; i>=0; i--){
      Integer value = 0
      for(int j=0; j<=index; j++){
        if(j % divisor == 0){
          value++
        }
      }
      if(rule?.style?.levels[i]?.sequence?.value == 'reset'){
        if(value%rule?.style?.levels[i]?.units == 0){
          value = rule?.style?.levels[i]?.units
        }else{
          value = value%rule?.style?.levels[i]?.units
        }
      }

      String stringValue = value
      if(rule?.style?.levels[i]?.format?.value == 'ordinal'){
        stringValue = value + getOrdinalSuffix(value)
      }

      if(rule?.style?.levels[i]?.format?.value == 'roman'){
        stringValue = intToRoman(value)
      }

      result.add([value: stringValue, level: i+1])

      divisor = rule?.style?.levels[i]?.units*divisor
    }
    return result.reverse()
  }
}