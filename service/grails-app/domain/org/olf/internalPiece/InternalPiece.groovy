package org.olf.internalPiece

import java.time.LocalDate

import grails.gorm.MultiTenant

public abstract class InternalPiece implements MultiTenant<InternalPiece> {
  String id

  static mapping = {
    id column: 'ip_id', generator: 'uuid2', length: 36
    version column: 'ip_version'


    tablePerHierarchy false
  }

  // Cannot handle ommited combination, please god no
  public static Integer findIndexFromDate(ArrayList<InternalPiece> internalPieces, LocalDate date){
    Integer indexCounter = 0
    internalPieces.each { InternalPiece piece -> 
      if(piece instanceof InternalRecurrencePiece || piece instanceof InternalOmissionPiece){
        if(piece.date == date){
          return indexCounter
        }else{
          indexCounter++
        }
      }else if(piece instanceof InternalCombinationPiece){
        piece.recurrencePieces.each { InternalRecurrencePiece combinedPiece ->
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
}
