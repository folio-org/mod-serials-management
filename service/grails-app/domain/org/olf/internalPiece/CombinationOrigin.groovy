package org.olf.internalPiece

import grails.gorm.MultiTenant

import java.time.LocalDate

import org.olf.combination.CombinationRule

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationOrigin implements MultiTenant<CombinationOrigin> {

  String id

  CombinationRule combinationRule

  static belongsTo = [
    owner: InternalCombinationPiece
  ]


  static mapping = {
    id column: 'co_id', generator: 'uuid2', length: 36
    combinationRule column: 'co_combination_rule_fk'
    owner column: 'co_owner_fk'
    version column: 'co_version'
  }

  static constraints = {
    owner(nullable:false, blank:false);
    combinationRule(nullable:false, blank:false);
  }
}
