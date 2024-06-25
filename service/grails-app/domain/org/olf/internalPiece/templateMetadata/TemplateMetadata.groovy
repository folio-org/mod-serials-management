package org.olf.internalPiece.templateMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

public class TemplateMetadata implements MultiTenant<TemplateMetadata> {
  String id

  static hasOne = [
   	standard: StandardTemplateMetadata
  ]

  static hasMany = [
    userConfigured: UserConfiguredTemplateMetadata
  ]

  static mapping = {
    id column: 'tm_id', generator: 'uuid2', length: 36
    version column: 'tm_version'
    userConfigured cascade: 'all-delete-orphan', sort: 'index', order: 'asc'

    tablePerHierarchy false
  }

  static constraints = {
    standard nullable: false
    userConfigured nullable: false
  }
}
