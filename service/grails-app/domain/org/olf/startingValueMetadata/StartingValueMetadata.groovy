package org.olf.startingValueMetadata

package org.olf.startingValueMetadata.startingValueMetadataFormat.StartingValueMetadataFormat

import org.olf.PredictedPieceSet

import java.time.LocalDate

import grails.gorm.MultiTenant

public class StartingValueMetadata implements MultiTenant<StartingValueMetadata> {
  String id

  PredictedPieceSet owner

  Integer index

  String startingValueMetadataFormat 

  @BindUsing({ StartingValueMetadataFormat obj, SimpleMapDataBindingSource source ->
		StartingValueMetadataFormatHelpers.doFormatBinding(obj, source)
  })
  StartingValueMetadataFormat format

  static hasOne = [
   	format: StartingValueMetadataFormat
  ]

  static mapping = {
    id column: 'svm_id', generator: 'uuid2', length: 36
    owner column: 'svm_owner_fk'
    version column: 'svm_version'
    index column: 'svm_index'
    startingValueMetadataFormat column: 'svm_starting_value_template_metadata_format'
	  format cascade: 'all-delete-orphan'
  }

   static constraints = {
    owner nullable: false
    index nullable: true
    startingValueMetadataFormat nullable: false
    format nullable: false, validator: StartingValueMetadataFormatHelpers.ruleTypeValidator
  }
}
