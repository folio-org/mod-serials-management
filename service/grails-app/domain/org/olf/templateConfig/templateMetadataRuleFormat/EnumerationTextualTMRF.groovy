package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.EnumerationTemplateMetadataRule
import org.olf.internalPiece.templateMetadata.EnumerationUCTMT

import java.time.LocalDate

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationTextualTMRF extends EnumerationTemplateMetadataRuleFormat implements MultiTenant<EnumerationTextualTMRF> {  
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

  private static String findResultIndex(EnumerationTemplateMetadataRule rule, int index){
    ArrayList<EnumerationTextualLevelTMRF> etltmrfArray = rule?.ruleFormat?.levels?.sort { it?.index }
    while (true) {
      for (int i = 0; i < etltmrfArray?.size(); i++) {
        index -= etltmrfArray[i]?.units;
        if (index <= 0) {
          return etltmrfArray[i]?.value;
        }    
      }
    }
  }

  public static EnumerationUCTMT handleFormat (EnumerationTemplateMetadataRule rule, LocalDate date, int index, EnumerationUCTMT startingValues){
    String result = findResultIndex(rule, index + 1)
    return new EnumerationUCTMT([value: result])
  }
}
