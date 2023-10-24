package org.olf.internalPiece.internalPieceLabel

import org.olf.label.LabelRule

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class InternalPieceEnumerationLabel extends InternalPieceLabel implements MultiTenant<InternalPieceEnumerationLabel> {

  ArrayList<EnumerationLabelLevel> levels

  static mapping = {
    levels cascade: 'all-delete-orphan'
  }
}
