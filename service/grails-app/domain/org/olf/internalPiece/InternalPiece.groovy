package org.olf.internalPiece

import grails.gorm.MultiTenant

public abstract class InternalPiece implements MultiTenant<InternalPiece> {
  String id

  static mapping = {
    id column: 'ip_id', generator: 'uuid2', length: 36
    version column: 'ip_version'


    tablePerHierarchy false
  }
}
