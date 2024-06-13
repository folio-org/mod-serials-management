package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationTemplateMetadata extends TemplateMetadataFormat implements MultiTenant<EnumerationTemplateMetadata> {

  String value
  Set<EnumerationTemplateMetadataLevel> levels

  static hasMany = [
    levels: EnumerationLevelSVMF,
  ]

  static mapping = {
    value column: 'etm_value'
    levels cascade: 'all-delete-orphan', sort: 'index', order: 'asc'
  }
  
  static constraints = {
    value nullable: true
    levels nullable: true
  }
}
