package org.olf.internalPiece.templateMetadata

import org.olf.SerialRuleset

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class UserConfiguredTemplateMetadata implements MultiTenant<UserConfiguredTemplateMetadata> {
  String id
  TemplateMetadata owner

  Integer index

  @CategoryId(value="UserConfiguredTemplateMetadata.UserConfiguredTemplateMetadataType", defaultInternal=true)
  @Defaults(['Chronology', 'Enumeration'])
  RefdataValue userConfiguredTemplateMetadataType

  // @BindUsing({ UserConfiguredTemplateMetadata obj, SimpleMapDataBindingSource source ->
	// 	UserConfiguredTemplateMetadataHelpers.doMetadataTypeBinding(obj, source)
  // })
  UserConfiguredTemplateMetadataType metadataType

  static hasOne = [
    metadataType: UserConfiguredTemplateMetadataType
  ]

  static belongsTo = [
    owner: TemplateMetadata
 	]

  static mapping = {
    id column: 'uctm_id', generator: 'uuid2', length: 36
    owner column: 'uctm_owner_fk'
    version column: 'uctm_version'
    index column: 'uctm_index'
  }

  static constraints = {
    owner nullable: false
    index nullable: false
    metadataType nullable: false, validator: UserConfiguredTemplateMetadataHelpers.metadataTypeValidator
  }
}
