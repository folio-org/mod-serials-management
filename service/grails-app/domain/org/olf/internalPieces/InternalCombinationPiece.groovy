package org.olf.internalPieces

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class InternalCombinationPiece extends InternalPiece implements MultiTenant<InternalCombinationPiece> {

  Set<InternalRecurrencePiece> recurrencePieces

  static mapping = {
    recurrencePieces cascade: 'all-delete-orphan'
  }
}
