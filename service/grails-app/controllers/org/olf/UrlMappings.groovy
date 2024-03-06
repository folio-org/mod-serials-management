package org.olf

class UrlMappings {

  static mappings = {
    "/"(controller: 'application', action:'index');

    '/serials-management/refdata'(resources: 'refdata') {
      collection {
        "/$domain/$property" (controller: 'refdata', action: 'lookup', method: 'GET')
      }
    }

    "/serials-management/serials" (resources: 'serial')

    "/serials-management/rulesets" (resources: 'serialRuleset')

    "/serials-management/predictedPieces" (resources: 'predictedPieceSet') {
      collection {
        "/generate" (controller: 'predictedPieceSet', action: 'generatePredictedPiecesTransient')
        "/create" (controller: 'predictedPieceSet', action: 'generatePredictedPieces')
      }
    }


    
  }
}
