package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

// This houses "standard" meta data about the piece for templating
public class StandardTemplateMetadata implements MultiTenant<StandardTemplateMetadata> {
  String id

  TemplateMetadata owner

  LocalDate date
  // This is the combination adjusted index for this piece
  Integer index
  // Naive index is the top level index in the list of internal pieces, not taking combination pieces internal pieces into account
  Integer naiveIndex
  // TODO Requires an index class incase of saving later, templateMetadata.standardTemplatemetadata
  // For a recurrence this will be the same as index, for combination pieces it will be an array of indices starting at index and counting up as far as there are contained pieces
  ArrayList<Integer> containedIndices

  static mapping = {
    id column: 'stm_id', generator: 'uuid2', length: 36
    owner column: 'stm_owner_fk'
    version column: 'stm_version'
    date column: 'stm_date'
    index column: 'stm_index'
    naiveIndex column: 'stm_naive_index'
  }

  static constraints = {
    owner nullable: false
  }

}
