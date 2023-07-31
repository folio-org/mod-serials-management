package org.olf.internalPiece

import grails.gorm.MultiTenant

import java.time.LocalDate

import org.olf.omission.OmissionRule

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionOrigin implements MultiTenant<OmissionOrigin> {

  OmissionRule omissionRule

  static belongsTo = [
    owner: InternalOmissionPiece
  ]


  static mapping = {
    omissionRule column: 'oo_omission_rule_fk'
    owner column: 'oo_owner_fk'
  }

  static constraints = {
    owner(nullable:false, blank:false);
    omissionRule(nullable:false, blank:false);
  }
}
