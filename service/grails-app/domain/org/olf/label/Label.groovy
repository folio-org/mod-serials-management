package org.olf.label

import org.olf.label.LabelRule
import org.olf.SerialRuleset

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class Label implements MultiTenant<Label> {
  String id
  SerialRuleset owner

  Set<LabelRule> rules

  static belongsTo = [
    owner: SerialRuleset
 	]

  static hasMany = [
    rules : LabelRule
  ]

  static mapping = {
          id column: 'l_id', generator: 'uuid2', length: 36
       owner column: 'l_owner_fk'
     version column: 'l_version'
      rules cascade: 'all-delete-orphan'
  }

    static constraints = {
         owner nullable: false
         rules nullable: false
    }
}
