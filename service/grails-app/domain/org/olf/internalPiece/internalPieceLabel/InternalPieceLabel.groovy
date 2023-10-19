package org.olf.internalPiece.internalPieceLabel

import java.time.LocalDate

import grails.gorm.MultiTenant

public abstract class InternalPieceLabel implements MultiTenant<InternalPieceLabel> {
  String id

  // TODO Add mappings to migrations

  static mapping = {
    id column: 'ipl_id', generator: 'uuid2', length: 36
    version column: 'ipl_version'


    tablePerHierarchy false
  }
}
