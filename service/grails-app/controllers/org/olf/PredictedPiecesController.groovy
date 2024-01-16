package org.olf

import org.olf.PieceGenerationService

import org.olf.Serial
import org.olf.SerialRuleset
import org.olf.internalPiece.InternalPiece

import java.time.LocalDate

import grails.rest.*
import grails.converters.*
import org.json.JSONObject
import grails.gorm.transactions.Transactional
import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j

import java.util.regex.Pattern

@Slf4j
@CurrentTenant
class PredictedPiecesController {
  PieceGenerationService pieceGenerationService

  // PredictedPiecesController(){
  //   super()
  // }
  
  // This takes in a JSON shape and outputs predicted pieces without saving domain objects
 @Transactional
  def generatePredictedPiecesTransient() {
    JSONObject data = request.JSON
    // SerialRuleset ruleset = new SerialRuleset(data.toMap()).save(flush: true, failOnError: true)
    SerialRuleset ruleset = new SerialRuleset(data.toMap())

    // TODO Should we validate this?
    ArrayList<InternalPiece> result = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate))
    respond result
  }

  def generatePredictedPieces() {
    JSONObject data = request.JSON
    Serial serial = Serial.get(data?.serial?.id)
    SerialRuleset ruleset = SerialRuleset.get(data?.ruleset?.id)
    ArrayList<InternalPiece> result = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate))

    respond result

  }
 }