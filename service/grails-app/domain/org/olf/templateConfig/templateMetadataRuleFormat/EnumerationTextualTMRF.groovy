package org.olf.templateConfig.templateMetadataRuleFormat

import org.olf.templateConfig.templateMetadataRule.EnumerationTemplateMetadataRule
import org.olf.internalPiece.templateMetadata.EnumerationUCTMT

import java.time.LocalDate

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.RefdataCategory

public class EnumerationTextualTMRF extends EnumerationTemplateMetadataRuleFormat implements MultiTenant<EnumerationTextualTMRF> {

  Set<EnumerationTextualLevelTMRF> levels

  RefdataCategory refdataCategory

  static hasMany = [
    levels: EnumerationTextualLevelTMRF,
  ]

  static mappedBy = [
    levels: 'owner',
  ]

  static mapping = {
    refdataCategory column: 'ettmrf_refdata_category_fk'
    levels cascade: 'all-delete-orphan', sort: 'index', order: 'asc'
  }
  
  static constraints = {
    refdataCategory nullable: false
    levels nullable: false
  }

  def beforeValidate() {
    // An issue exists where if a RefdataCategory desc is the only property passed down then a new RefdataCategory would be generated
    // This check ensures that if this occurs, the RefdataCategory will attempt to be found based on the desc and the property will be set to the result
    if(this.refdataCategory?.id === null && this.refdataCategory.hasProperty('desc')){
      RefdataCategory rdc = RefdataCategory.findByDesc(this.refdataCategory.desc)
      this.refdataCategory = rdc
    }
  }


  private static String findResultIndex(EnumerationTemplateMetadataRule rule, int index) {
    ArrayList<EnumerationTextualLevelTMRF> etltmrfArray = rule?.ruleFormat?.levels?.sort { it?.index }
    while (true) {
      for (int i = 0; i < etltmrfArray?.size(); i++) {
        index -= etltmrfArray[i]?.units;
        if (index <= 0) {
          return etltmrfArray[i]?.getValue();
        }
      }
    }
  }

  public static EnumerationUCTMT handleFormat (EnumerationTemplateMetadataRule rule, LocalDate date, int index, EnumerationUCTMT startingValues){
    String result = findResultIndex(rule, index + 1)
    return new EnumerationUCTMT([value: result])
  }
}
