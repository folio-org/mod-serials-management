package org.olf.internalPieces

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class InternalOmissionPiece extends InternalPiece implements MultiTenant<InternalOmissionPiece> {

  LocalDate date

  static mapping = {
    date column: 'iop_date'
  }
}
