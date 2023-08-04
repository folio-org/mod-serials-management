package org.olf

import org.olf.PieceGenerationService

import org.olf.SerialRuleset
import org.olf.internalPiece.InternalPiece

import java.time.LocalDate

import grails.rest.*
import grails.converters.*
import org.grails.web.json.JSONObject
import grails.gorm.transactions.Transactional
import grails.gorm.multitenancy.CurrentTenant
import groovy.json.JsonOutput
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
    final data = request.JSON
    // Do not save this -- Is casting this all in one go ok?
    // FIXME DO NOT SAVE
    // SerialRuleset ruleset = new SerialRuleset(data).save(flush: true, failOnError: true)
    SerialRuleset ruleset = new SerialRuleset(data)
    // TODO Should we validate this?
    ArrayList<InternalPiece> result = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate))
    respond result
  }

  def generatePredictedPieces() {

  }
 }