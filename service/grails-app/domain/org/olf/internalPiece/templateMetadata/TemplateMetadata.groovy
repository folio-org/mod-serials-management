package org.olf.internalPiece.templateMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

public abstract class TemplateMetadata implements MultiTenant<TemplateMetadata> {
  String id

  static mapping = {
    tablePerHierarchy false
  }
}
