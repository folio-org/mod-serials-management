package org.olf

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

  // PredictedPiecesController(){
  //   super()
  // }

  def generatePredictedPiecesJson()

  def generatePredictedPieces()
 }