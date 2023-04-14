package org.olf.combination

import org.olf.combination.CombinationRule
import org.olf.SerialRuleset

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class Combination implements MultiTenant<Combination> {
  String id
  SerialRuleset owner

  Set<CombinationRule> rules

  static belongsTo = [
    owner: SerialRuleset
 	]

  static hasMany = [
    rules : CombinationRule
  ]

  static mapping = {
          id column: 'c_id', generator: 'uuid2', length: 36
       owner column: 'c_owner_fk'
     version column: 'c_version'
      rules cascade: 'all-delete-orphan'
  }

    static constraints = {
         owner nullable: false
         rules nullable: false
    }
}
