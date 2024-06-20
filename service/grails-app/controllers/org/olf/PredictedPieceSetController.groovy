package org.olf

import org.olf.PieceGenerationService
import org.olf.PieceLabellingService

import org.olf.Serial
import org.olf.SerialRuleset
import org.olf.PredictedPieceSet
import org.olf.internalPiece.templateMetadata.TemplateMetadata
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
    TemplateMetadata nextPieceTemplateMetadata = pieceLabellingService.generateTemplateMetadataForPiece(nextPiece, ips, ruleset?.templateConfig, startingValues)

    TemplateMetadata firstPieceTemplateMetadata = pieceLabellingService.generateTemplateMetadataForPiece(ips?.get(0), ips, ruleset?.templateConfig, startingValues)

    // FIXME As it currently stands, the "nextPiece" gets appended to the list of internal peices for use within the labelling service to figure out index, naive index etc.
    // We should need to remove the the final element in the array just before save, theres a better way of doing this
    ips.pop()

    PredictedPieceSet pps = new PredictedPieceSet([
      ruleset: ruleset,
      pieces: ips,
      note: data?.note,
      startDate: data?.startDate,
      firstPieceTemplateMetadata: firstPieceTemplateMetadata,
      nextPieceTemplateMetadata: nextPieceTemplateMetadata
    ]).save(flush: true, failOnError: true)

    respond pps

  }
}