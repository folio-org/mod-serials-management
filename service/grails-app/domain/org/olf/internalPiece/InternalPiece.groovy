package org.olf.internalPiece

import org.olf.PredictedPieceSet

import org.olf.internalPiece.templateMetadata.TemplateMetadata

import java.time.LocalDate

import grails.gorm.MultiTenant

public abstract class InternalPiece implements MultiTenant<InternalPiece> {
  String id

  String templateString
  String label

  String receivingId

  // TODO We may wish to store this on the internal piece at some point
  // TemplateMetadata templateMetadata

 	static belongsTo = [ owner: PredictedPieceSet ]

  static mappedBy = [
    receivingPieces: 'owner'
  ]

  static hasMany = [
    receivingPieces: ReceivingPiece
  ]

  static mapping = {
    id column: 'ip_id', generator: 'uuid2', length: 36
    version column: 'ip_version'
    owner column: 'ip_owner'
    templateString column: 'ip_template_string'
    label column: 'ip_label'
    receivingId column: 'ip_receiving_id'

    tablePerHierarchy false
  }

  static constraints = {
    templateString nullable: true
    receivingId nullable: true
    label nullable: true
    owner nullable: true
  }

  // Cannot handle ommited combination, please god no
  // This is used to generate a list with omissions as real pieces
  public static Integer findIndexFromDate(ArrayList<InternalPiece> internalPieces, LocalDate date){
    Integer indexCounter = 0
    for(InternalPiece piece in internalPieces){
      if(piece instanceof InternalRecurrencePiece || piece instanceof InternalOmissionPiece){
        if(piece.date == date){
          return indexCounter
        }else{
          indexCounter++
        }
      }else if(piece instanceof InternalCombinationPiece){
        for(InternalRecurrencePiece combinedPiece in piece.recurrencePieces.sort{ a,b -> a.date <=> b.date }){
          if(combinedPiece.date == date){
            return indexCounter
          }else{
            indexCounter++
          }
        }
      }
    }
    return -1
  }

  public static ArrayList<InternalPiece> conditionalGroupRecurrencePieces(ArrayList<InternalPiece> internalPieces, Closure condition){
    ArrayList<InternalPiece> group = []
    internalPieces.each { ip ->
      if(ip instanceof InternalCombinationPiece){
        ip.recurrencePieces.findAll(condition).each { rp -> 
          group << rp
        }
      }else if((ip instanceof InternalRecurrencePiece || ip instanceof InternalOmissionPiece) && condition.call(ip)){
        group << ip
      }
    }
    return group
  }
}
