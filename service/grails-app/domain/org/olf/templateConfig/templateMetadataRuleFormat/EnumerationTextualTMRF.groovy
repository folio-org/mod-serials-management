package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRule
import org.olf.internalPiece.templateMetadata.EnumerationTemplateMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationTextualTMRF extends TemplateMetadataRuleFormat implements MultiTenant<EnumerationTextualTMRF> {  
  Set<EnumerationTextualLevelTMRF> levels

  static hasMany = [
    levels: EnumerationTextualLevelTMRF,
  ]

  static mapping = {
    levels cascade: 'all-delete-orphan', sort: 'index', order: 'asc'
  }
  
  static constraints = {
    levels nullable: false
  }

  private static String findResultIndex(TemplateMetadataRule rule, int index){
    ArrayList<EnumerationTextualLevelTMRF> etltmrfArray = rule?.ruleType?.ruleFormat?.levels?.sort { it?.index }
    while (true) {
      for (int i = 0; i < etltmrfArray?.size(); i++) {
        index -= etltmrfArray[i]?.units;
        if (index <= 0) {
          return etltmrfArray[i]?.value;
        }    
      }
    }
  }

  public static EnumerationTemplateMetadata handleFormat (TemplateMetadataRule rule, LocalDate date, int index, Map startingValues){
    String result = findResultIndex(rule, index + 1)
    return new EnumerationTemplateMetadata([value: result])
  }
}
