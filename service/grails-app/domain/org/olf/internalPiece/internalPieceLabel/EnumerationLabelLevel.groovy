package org.olf.internalPiece.internalPieceLabel

import org.olf.label.enumerationLevel.EnumerationLevel

import org.olf.label.LabelRule

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class EnumerationLabelLevel implements MultiTenant<EnumerationLabelLevel> {

  String value
  Integer level

  // EnumerationLevel enumerationRule

  static mapping = {
    value column: 'ell_value'
    level column: 'ell_level'
    enumerationRule column: 'ell_enumeration_rule_fk'
  }

  static constraints = {
    value nullable : false
    level nullable: false
    enumerationRule nullable: false
  }
}
