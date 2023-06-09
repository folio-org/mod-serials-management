package org.olf

import org.olf.PieceGenerationService

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
  def generatePredictedPiecesJson() {
    final data = request.JSON
    final result = pieceGenerationService.createPiecesJson(data)
    render result as JSON
  }

  def generatePredictedPieces() {

  }
 }