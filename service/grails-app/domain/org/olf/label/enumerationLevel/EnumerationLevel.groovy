package org.olf.label.enumerationLevel

import org.olf.label.labelStyle.LabelStyleEnumeration

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class EnumerationLevel implements MultiTenant<EnumerationLevel> {

  String id
  Integer units

  @CategoryId(value="EnumerationLevel.Format", defaultInternal=true)
  @Defaults(['Ordinal', 'Number', 'Roman'])
  RefdataValue format

  @CategoryId(value="EnumerationLevel.Sequence", defaultInternal=true)
  @Defaults(['Reset', 'Continuous'])
  RefdataValue sequence

  String internalNote


  static belongsTo = [
    owner: LabelStyleEnumeration
  ]

  static mapping = {
              id column: 'el_id', generator: 'uuid2', length: 36
           owner column: 'el_owner_fk'
         version column: 'el_version'
           units column: 'el_units'
          format column: 'el_format_fk'
        sequence column: 'el_sequence_fk'
    internalNote column: 'el_internal_note'

  }
  
  static constraints = {
    owner(nullable:false, blank:false);
    units nullable: false
    format nullable: false
    sequence nullable: false
    internalNote nullable: true
  }   
}

