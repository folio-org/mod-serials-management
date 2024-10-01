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
import java.util.regex.Pattern

import static org.springframework.http.HttpStatus.*

import grails.rest.*
import grails.converters.*
import grails.gorm.transactions.Transactional
import grails.gorm.multitenancy.CurrentTenant

import org.grails.web.json.JSONObject
import org.grails.web.json.JSONArray

import groovy.util.logging.Slf4j

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
    JSONArray startingValuesJson = data?.startingValues ?: []
    
    SerialRuleset ruleset = new SerialRuleset(data)

    //TODO Not super happy with the implementation of this conditional, however the JSONArray .get() freaks out over null array elements vs empty
    // This conditional is to check if the starting array contains elements and if they are of the older/newer shape
    if(!startingValuesJson?.toString()?.contains('userConfiguredTemplateMetadataType') && startingValuesJson.size()){
    pieceLabellingService.updateStartingValuesShape(startingValuesJson)
    }

    ArrayList<UserConfiguredTemplateMetadata> startingValues = new ArrayList<UserConfiguredTemplateMetadata>(startingValuesJson)

    ArrayList<InternalPiece> ips = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate))
    pieceLabellingService.setLabelsForInternalPieces(ips, ruleset?.templateConfig, startingValues)

    respond ips
  }

  def generatePredictedPieces() {
    JSONObject data = request.JSON
    SerialRuleset ruleset = SerialRuleset.get(data?.id)
    JSONArray startingValuesJson = data?.startingValues ?: []

    if(!startingValuesJson?.toString()?.contains('userConfiguredTemplateMetadataType') && startingValuesJson.size()){
    pieceLabellingService.updateStartingValuesShape(startingValuesJson)
    }

    ArrayList<UserConfiguredTemplateMetadata> startingValues = new ArrayList<UserConfiguredTemplateMetadata>(data?.startingValues ?: [])

    ArrayList<InternalPiece> ips = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate))
    pieceLabellingService.setLabelsForInternalPieces(ips, ruleset?.templateConfig, startingValues)

    InternalPiece nextPiece = pieceGenerationService.generateNextPiece(ips.get(ips.size()-1), ruleset)
    TemplateMetadata nextPieceTemplateMetadata = pieceLabellingService.generateTemplateMetadataForPiece(nextPiece, ips, ruleset?.templateConfig, startingValues)

    TemplateMetadata firstPieceTemplateMetadata = pieceLabellingService.generateTemplateMetadataForPiece(ips?.get(0), ips, ruleset?.templateConfig, startingValues)

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

  @Transactional
  def delete() {
    PredictedPieceSet pps = queryForResource(params.id)

    if (pps == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    // Return the relevant status if not allowed to delete.
    if (pps.pieces?.any {p -> p?.receivingPieces?.size() >= 1}) {
      transactionStatus.setRollbackOnly()
      render status: METHOD_NOT_ALLOWED, text: "Cannot delete a predicted piece set which contains receiving pieces"
      return
    }

    // Finally delete the predicted piece set if we get this far and respond.
    deleteResource pps
    render status: NO_CONTENT
  }
}