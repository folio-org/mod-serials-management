package org.olf.internalPiece.templateMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

// TODO TemplateMetadata
public abstract class TemplateMetadata implements MultiTenant<TemplateMetadata> {
  String id

  // TODO Add mappings to migrations

  static mapping = {
    id column: 'ipl_id', generator: 'uuid2', length: 36
    version column: 'ipl_version'


    tablePerHierarchy false
  }
}
