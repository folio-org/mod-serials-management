package org.olf.internalPiece

import grails.gorm.MultiTenant

import java.time.LocalDate

import org.olf.omission.OmissionRule

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class InternalOmissionPiece extends InternalPiece implements MultiTenant<InternalOmissionPiece> {

  LocalDate date

  OmissionRule omissionRule


  static mapping = {
    date column: 'iop_date'
    omissionRule column: 'iop_omission_rule_fk'
  }

  static constraints = {
    omissionRule nullable: true
  }
}
