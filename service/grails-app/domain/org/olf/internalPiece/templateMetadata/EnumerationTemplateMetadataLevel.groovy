package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationTemplateMetadataLevel implements MultiTenant<EnumerationTemplateMetadataLevel> {

  String value
  Integer level


  static mapping = {
    value column: 'ell_value'
    level column: 'ell_level'
  }

  static constraints = {
    value nullable : false
    level nullable: false
  }
}
