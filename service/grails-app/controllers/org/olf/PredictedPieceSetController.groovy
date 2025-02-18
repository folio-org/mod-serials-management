package org.olf

import org.olf.PieceGenerationService
import org.olf.PieceLabellingService

import org.olf.Serial
import org.olf.SerialRuleset
import org.olf.PredictedPieceSet
import org.olf.internalPiece.templateMetadata.TemplateMetadata
import org.olf.internalPiece.templateMetadata.UserConfiguredTemplateMetadata
import org.olf.internalPiece.InternalPiece
import org.olf.templating.LabelTemplateBindings

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

  private PredictedPieceSet setupPredictedPieces(JSONObject data, JSONArray startingValuesJson, SerialRuleset ruleset) {
    ArrayList<UserConfiguredTemplateMetadata> startingValues = new ArrayList<UserConfiguredTemplateMetadata>(startingValuesJson)

    Integer numberOfCycles = data?.numberOfCycles as Integer ?: 1

    ArrayList<InternalPiece> ips = pieceGenerationService.createPiecesTransient(ruleset, LocalDate.parse(data.startDate), numberOfCycles)

    TemplateMetadata initialPieceRecurrenceMetadata = pieceLabellingService.generateTemplateMetadataForPiece(ips?.get(0), ips, ruleset?.templateConfig, startingValues, null)

    // Check for omission rules within the ruleset
    // Since we presently only handle omissions OR combinations, only one should ever been applied to the internal pieces
    if (!!ruleset?.omission) {
      pieceGenerationService.applyOmissionRules(ips, ruleset)
    } else if (!!ruleset?.combination) {
      pieceGenerationService.applyCombinationRules(ips, ruleset)
    }
    // TODO This should be seperated out into one method setting labels for pieces and another that grabs last piece template bindings
    LabelTemplateBindings lastPieceLabelTemplateBindings = pieceLabellingService.setLabelsForInternalPieces(ips, ruleset?.templateConfig, startingValues)

    InternalPiece nextPiece = pieceGenerationService.generateNextPiece(ips.get(ips.size()-1), ruleset)
    TemplateMetadata continuationPieceRecurrenceMetadata = pieceLabellingService.generateTemplateMetadataForPiece(nextPiece, ips, ruleset?.templateConfig, startingValues, lastPieceLabelTemplateBindings?.enumerationArray)

    PredictedPieceSet pps = new PredictedPieceSet([
      ruleset: ruleset,
      pieces: ips,
      note: data?.note,
      startDate: data?.startDate,
      numberOfCycles: numberOfCycles,
      initialPieceRecurrenceMetadata: initialPieceRecurrenceMetadata,
      continuationPieceRecurrenceMetadata: continuationPieceRecurrenceMetadata
    ])

    return pps
    
  }

  // This takes in a JSON shape and outputs predicted pieces without saving domain objects
  def generatePredictedPiecesTransient() {
    JSONObject data = request.JSON
    JSONArray startingValuesJson = data?.startingValues ?: []
    
    SerialRuleset ruleset = new SerialRuleset(data)

    PredictedPieceSet pps = setupPredictedPieces(data, startingValuesJson, ruleset)

    respond pps
  }

  @Transactional
  def generatePredictedPieces() {
    JSONObject data = request.JSON
    JSONArray startingValuesJson = data?.startingValues ?: []

    SerialRuleset ruleset = SerialRuleset.get(data?.id)

    PredictedPieceSet pps = setupPredictedPieces(data, startingValuesJson, ruleset)
    // TODO Check that this should be a flush
    pps.save(flush: true, failOnError: true)
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