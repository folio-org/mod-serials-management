package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationTemplateMetadataLevel implements MultiTenant<EnumerationTemplateMetadataLevel> {

  String value

  static constraints = {
    value nullable : false
  }
}
