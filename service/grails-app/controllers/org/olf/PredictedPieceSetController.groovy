package org.olf

import org.olf.PieceGenerationService

import org.olf.Serial
import org.olf.SerialRuleset
import org.olf.PredictedPieceSet
import org.olf.internalPiece.InternalPiece

import com.k_int.okapi.OkapiTenantAwareController

import java.time.LocalDate

import grails.rest.*
import grails.converters.*
import org.grails.web.json.JSONObject
import grails.gorm.transactions.Transactional
import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j

import java.util.regex.Pattern

@Slf4j
@CurrentTenant
class PredictedPieceSetController extends OkapiTenantAwareController<PredictedPieceSet> {
  PredictedPieceSetController(){
    super(PredictedPieceSet)
  }
  PieceGenerationService pieceGenerationService

  // Starting values will be refactored soon so this is a placeholder to ensure they are passed through presently
  private SerialRuleset handleStartingValues(SerialRuleset ruleset, JSONObject data){
    if(ruleset?.templateConfig?.rules?.size()){
        for(int i=0;i<ruleset?.templateConfig?.rules?.size();i++){
          if(ruleset?.templateConfig?.rules[i]?.ruleType?.templateMetadataRuleFormat?.value == 'enumeration_numeric'){
            for(int j=0;j<ruleset?.templateConfig?.rules[i]?.ruleType?.ruleFormat?.levels?.size();j++){
              if(data?.templateConfig?.rules?.getAt(i)?.ruleType?.ruleFormat?.levels?.getAt(j)?.startingValue){
                ruleset?.templateConfig?.rules[i]?.ruleType?.ruleFormat?.levels[j]?.startingValue = Integer.parseInt(data?.templateConfig?.rules[i]?.ruleType?.ruleFormat?.levels[j]?.startingValue)
              }
            }
          }
        }
      }
    return ruleset  
  }
  // This takes in a JSON shape and outputs predicted pieces without saving domain objects
  def generatePredictedPiecesTransient() {
    JSONObject data = request.JSON
    SerialRuleset ruleset = new SerialRuleset(data)

    ArrayList<InternalPiece> result = pieceGenerationService.createPiecesTransient(handleStartingValues(ruleset, data), LocalDate.parse(data.startDate))
    respond result
  }

  def generatePredictedPieces() {
    JSONObject data = request.JSON
    SerialRuleset ruleset = SerialRuleset.get(data?.id)

    ArrayList<InternalPiece> ips = pieceGenerationService.createPiecesTransient(handleStartingValues(ruleset, data), LocalDate.parse(data.startDate))

    PredictedPieceSet pps = new PredictedPieceSet([
      ruleset: ruleset,
      pieces: ips,
      note: data?.note,
      startDate: data?.startDate
    ]).save(flush: true, failOnError: true)

    respond pps

  }
}