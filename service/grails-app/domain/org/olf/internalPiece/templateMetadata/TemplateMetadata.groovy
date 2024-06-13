package org.olf.internalPiece.templateMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

public class TemplateMetadata implements MultiTenant<TemplateMetadata> {
  String id

  PredictedPieceSet owner

  Integer index

  String templateMetadataFormat

  @BindUsing({ TemplateMetadataFormat obj, SimpleMapDataBindingSource source ->
		TemplateMetadataFormatHelpers.doFormatBinding(obj, source)
  })
  TemplateMetadataFormat format

  static hasOne = [
   	format: TemplateMetadataFormat
  ]

  static mapping = {
    id column: 'tm_id', generator: 'uuid2', length: 36
    owner column: 'tm_owner_fk'
    version column: 'tm_version'
    index column: 'tm_index'
    startingValueMetadataFormat column: 'tm_template_metadata_format'
	  format cascade: 'all-delete-orphan'

    tablePerHierarchy false
  }

  static constraints = {
    owner nullable: false
    index nullable: true
    templateMetadataFormat nullable: false
    format nullable: false, validator: TemplateMetadataFormatHelpers.formatValidator
  }
}
