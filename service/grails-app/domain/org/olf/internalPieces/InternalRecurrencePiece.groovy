package org.olf.internalPieces

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class InternalRecurrencePiece extends InternalPiece implements MultiTenant<InternalRecurrencePiece> {

  LocalDate date

  static mapping = {
    date column: 'irp_date'
  }

  static constraint = {
  }
}
