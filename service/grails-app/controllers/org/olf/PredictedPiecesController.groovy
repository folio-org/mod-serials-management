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
import grails.web.Controller
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import groovy.json.JsonSlurper

import java.util.regex.Pattern

@Slf4j
@CurrentTenant
@Controller
class PredictedPiecesController {
  PieceGenerationService pieceGenerationService

  // PredictedPiecesController(){
  //   super()
  // }
  
  // This takes in a JSON shape and outputs predicted pieces without saving domain objects
 @Transactional
  def generatePredictedPiecesTransient() {
    // String dataString = request.JSON.toString()
    
    JSONObject data = request.JSON

    // def objToBind = getObjectToBind() // This will actually get the JSON in the cases where JSON has been sent and should be equivalent of request.JSON
    // SerialRuleset instance = new SerialRuleset()
    // bindData(instance, data)

    // Log as you do the others.
    // println("LOGDEBUG RULES INSTANCE: ${instance.templateConfig.rules.size()}")

    // final data = request.JSON
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
    Map grailsMap = new HashMap(request.JSON)

    println("LOGDEBUG MAPDATA CLASS: ${mapData.class}")

    println("LOGDEBUG DATA: ${data.toMap().templateConfig.rules}")
    println("LOGDEBUG GRAILSMAPDATA: ${grailsMap.templateConfig.rules}")
    println("LOGDEBUG MAPDATA: ${mapData.templateConfig.rules}")
    println("LOGDEBUG MAPDATA COMPARE DATA: ${mapData.equals(data.toMap())}")
    println("LOGDEBUG MAPDATA COMPARE GRAILSMAP: ${mapData.equals(grailsMap)}")


    // println("LOGDEBUG DATA MAP: ${data.toMap().templateConfig.rules}")
    SerialRuleset ruleset = new SerialRuleset(data.toMap())
    SerialRuleset ruleset2 = new SerialRuleset(mapData)
    SerialRuleset ruleset3 = new SerialRuleset(request.JSON)
    SerialRuleset ruleset4 = new SerialRuleset(grailsMap)

    println("LOGDEBUG RULES: ${ruleset.templateConfig.rules.size()}")
    println("LOGDEBUG RULES2: ${ruleset2.templateConfig.rules.size()}")
    println("LOGDEBUG RULES3: ${ruleset3.templateConfig.rules.size()}")
    println("LOGDEBUG RULES4: ${ruleset4.templateConfig.rules.size()}")

    // TODO Should we validate this?
    ArrayList<InternalPiece> result = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate))
    respond result
  }

  def generatePredictedPieces() {

  }
 }