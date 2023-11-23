package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRule
import org.olf.internalPiece.templateMetadata.EnumerationTemplateMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationCyclicalTMRF extends TemplateMetadataRuleFormat implements MultiTenant<EnumerationCyclicalTMRF> {
  String refdataDesc
  
  ArrayList<EnumerationCyclicalLevelTMRF> levels

  static mapping = {
    levels cascade: 'all-delete-orphan'
  }
  
  static constraints = {
    levels nullable: false
  }

  private static String findResultIndex(TemplateMetadataRule rule, int index){
    EnumerationCyclicalTMRF ectmrf = rule?.ruleType?.ruleFormat
    while (true) {
      for (int i = 0; i < ectmrf?.levels?.size(); i++) {
        if (index <= 0) {
          return ectmrf?.levels[i]?.selectedValue;
        }
        index -= ectmrf?.levels[i]?.units;
      }    
    }
  }

  public static EnumerationTemplateMetadata handleFormat (TemplateMetadataRule rule, LocalDate date, int index){
    String result = findResultIndex(rule, index)
    return new EnumerationTemplateMetadata([value: result])
  }
}
