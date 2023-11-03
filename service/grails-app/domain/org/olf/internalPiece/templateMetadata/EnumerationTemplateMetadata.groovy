package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

// TODO EnumerationTemplateMetadata
public class EnumerationTemplateMetadata extends TemplateMetadata implements MultiTenant<EnumerationTemplateMetadata> {

  ArrayList<EnumerationTemplateMetadataLevel> levels

  static mapping = {
    levels cascade: 'all-delete-orphan'
  }
}