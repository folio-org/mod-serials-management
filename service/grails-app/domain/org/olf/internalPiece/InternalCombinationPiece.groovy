package org.olf.internalPiece

import grails.gorm.MultiTenant

import java.time.LocalDate

import org.olf.combination.CombinationRule

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class InternalCombinationPiece extends InternalPiece implements MultiTenant<InternalCombinationPiece> {

  Set<InternalRecurrencePiece> recurrencePieces

  CombinationRule combinationRule

  static mapping = {
    recurrencePieces cascade: 'all-delete-orphan'
    combinationRule column: 'icp_combination_rule_fk'
  }

  static constraints = {
    combinationRule nullable: true
  }
}
