package org.olf

import org.olf.PieceGenerationService
import org.olf.PieceLabellingService

import org.olf.Serial
import org.olf.SerialRuleset
import org.olf.PredictedPieceSet
import org.olf.internalPiece.templateMetadata.UserConfiguredTemplateMetadata
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
  PieceLabellingService pieceLabellingService

  // This takes in a JSON shape and outputs predicted pieces without saving domain objects
  def generatePredictedPiecesTransient() {
    JSONObject data = request.JSON
    SerialRuleset ruleset = new SerialRuleset(data)
    ArrayList<UserConfiguredTemplateMetadata> startingValues = data?.startingValues ?: []

    ArrayList<InternalPiece> ips = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate))
    pieceLabellingService.setLabelsForInternalPieces(ips, ruleset?.templateConfig, startingValues)

    respond ips
  }

  def generatePredictedPieces() {
    JSONObject data = request.JSON
    SerialRuleset ruleset = SerialRuleset.get(data?.id)
    ArrayList<UserConfiguredTemplateMetadata> startingValues = data?.startingValues ?: []

    ArrayList<InternalPiece> ips = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate))
    pieceLabellingService.setLabelsForInternalPieces(ips, ruleset?.templateConfig, startingValues)

    InternalPiece nextPiece = pieceGenerationService.generateNextPiece(ips.get(ips.size()-1), ruleset)
    pieceLabellingService.setTemplateMetadataForPiece(nextPiece, ips, ruleset?.templateConfig, startingValues)

    pieceLabellingService.setTemplateMetadataForPiece(ips?.get(0), ips, ruleset?.templateConfig, startingValues)

    PredictedPieceSet pps = new PredictedPieceSet([
      ruleset: ruleset,
      pieces: ips,
      note: data?.note,
      startDate: data?.startDate,
      firstPiece: ips?.get(0),
      nextPiece: nextPiece
    ])
    // .save(flush: true, failOnError: true)

    respond pps

  }
}