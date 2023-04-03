package org.olf.omission

import org.olf.omission.OmissionRule
import org.olf.SerialRuleset

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class Omission implements MultiTenant<Omission> {
  String id
  SerialRuleset owner

  Set<OmissionRule> rules

  static belongsTo = [
    owner: SerialRuleset
 	]

  static hasMany = [
    rules : OmissionRule
  ]

  static mapping = {
          id column: 'o_id', generator: 'uuid2', length: 36
       owner column: 'o_owner_fk'
     version column: 'o_version'
      rules cascade: 'all-delete-orphan'
  }

    static constraints = {
         owner nullable: false
         rules nullable: false
    }
}
