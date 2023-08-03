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
    for(InternalPiece piece in internalPieces){
      if(piece instanceof InternalRecurrencePiece || piece instanceof InternalOmissionPiece){
        if(piece.date == date){
          return indexCounter
        }else{
          indexCounter++
        }
      }else if(piece instanceof InternalCombinationPiece){
        for(InternalRecurrencePiece combinedPiece in piece.recurrencePieces){
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

  public static ArrayList<InternalRecurrencePiece> conditionalGroupRecurrencePieces(ArrayList<InternalPiece> internalPieces, Closure condition){
    ArrayList<InternalRecurrencePiece> group = []
    internalPieces.each { ip ->
      if(ip instanceof InternalCombinationPiece){
        ip.recurrencePieces.findAll(condition).each { rp -> 
          group << rp
        }
      }else if((ip instanceof InternalRecurrencePiece) && condition.call(ip)){
        group << ip
      }
    }
    return group
  }
}
