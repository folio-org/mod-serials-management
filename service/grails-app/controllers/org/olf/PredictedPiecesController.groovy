package org.olf

import org.olf.PieceGenerationService

import org.olf.SerialRuleset

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
  def generatePredictedPiecesTransient() {
    final data = request.JSON
    // Do not save this -- Is casting this all in one go ok?
    SerialRuleset ruleset = new SerialRuleset(data)
    // TODO Should we validate this?
    final result = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate))
    render result as JSON
  }

  def generatePredictedPieces() {

  }
 }