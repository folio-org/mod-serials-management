package org.olf.internalPiece

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ReceivingPiece implements MultiTenant<ReceivingPiece> {

  String id

  String receivingId

  static belongsTo = [
    owner: InternalPiece
  ]

  static mapping = {
    id column: 'recp_id', generator: 'uuid2', length: 36
    receivingId column: 'recp_receiving_id'
    owner column: 'recp_owner_fk'
    version column: 'recp_version'
  }

  static constraints = {
    owner(nullable:false, blank:false);
    receivingId(nullable:false, blank:false);
  }
}
