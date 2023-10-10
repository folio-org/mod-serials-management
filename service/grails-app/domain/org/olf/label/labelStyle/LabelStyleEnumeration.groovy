package org.olf.label.labelStyle

import org.olf.label.enumerationLevel.EnumerationLevel

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class LabelStyleEnumeration extends LabelStyle implements MultiTenant<LabelStyleEnumeration> {
  static hasMany = [
    levels : EnumerationLevel,
  ]

  static mapping = {
    levels cascade: 'all-delete-orphan'
  }
  
  static constraints = {
    levels nullable: false
  }   
}
