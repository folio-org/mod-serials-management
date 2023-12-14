package org.olf

import org.olf.PieceGenerationService

import org.olf.SerialRuleset
import org.olf.internalPiece.InternalPiece

import java.time.LocalDate

import grails.rest.*
import grails.converters.*
import org.json.JSONObject
import org.json.JSONArray
import grails.gorm.transactions.Transactional
import grails.gorm.multitenancy.CurrentTenant
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import groovy.json.JsonSlurper

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
    // String dataString = request.JSON.toString()
    JSONObject request = new JSONObject(request.JSON)
    Map data = request.toMap()
    Map mapData = new HashMap([
    "rulesetStatus": [
        "value": "active"
    ],
    "recurrence": [
        "timeUnit": [
            "value": "day"
        ],
        "period": "1",
        "issues": "1",
        "rules": [
            [
                "ordinal": 1,
                "pattern": [:],
                "patternType": "day"
        ]
        ]
    ],
    "templateConfig": [
        "templateString": "ABC {{chronology1.year}}",
        "rules": [
            [
                "templateMetadataRuleType": "chronology",
                "ruleType": [
                    "templateMetadataRuleFormat": "chronology_year",
                    "ruleFormat": [
                        "yearFormat": [
                            "value": "slice"
        ]
        ]
        ]
        ]
        ]
    ],
    "patternType": "year_date",
    "startDate": "2023-10-11"
    ])
    
    // Do not save this -- Is casting this all in one go ok?
    // FIXME DO NOT SAVE
    // SerialRuleset ruleset = new SerialRuleset(data).save(flush: true, failOnError: true)

    println("LOGDEBUG MAPDATA CLASS: ${mapData.class}")

    println("LOGDEBUG DATA: ${data.templateConfig.rules}")
    println("LOGDEBUG MAPDATA: ${mapData.templateConfig.rules}")
    println("LOGDEBUG MAPDATA COMPARE DATA: ${mapData.equals(data)}")


    // println("LOGDEBUG DATA MAP: ${data.toMap().templateConfig.rules}")
    SerialRuleset ruleset = new SerialRuleset(data)
    SerialRuleset ruleset2 = new SerialRuleset(mapData)
    // SerialRuleset ruleset3 = new SerialRuleset(request.JSON)

    println("LOGDEBUG RULES: ${ruleset.templateConfig.rules.size()}")
    println("LOGDEBUG RULES2: ${ruleset2.templateConfig.rules.size()}")
    // println("LOGDEBUG RULES3: ${ruleset3.templateConfig.rules.size()}")

    // TODO Should we validate this?
    ArrayList<InternalPiece> result = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate))
    respond mapData
  }

  def generatePredictedPieces() {

  }
 }