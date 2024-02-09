package org.olf.internalPiece

import org.olf.PredictedPieceSet

import java.time.LocalDate

import grails.gorm.MultiTenant

public abstract class InternalPieceReceivingId implements MultiTenant<InternalPieceReceivingId> {
  String id

  String receivingId

 	static belongsTo = [ owner: InternalPiece ]

  static mapping = {
    id column: 'ipri_id', generator: 'uuid2', length: 36
    version column: 'ipri_version'
    owner column: 'ipri_owner'
    receivingId column: 'ipri_receiving_id'
  }

  static constraints = {
    owner nullable: false
    receivingId nullable: false
  }

}
