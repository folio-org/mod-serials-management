package org.olf

class UrlMappings {

  static mappings = {
    "/"(controller: 'application', action:'index');

    '/serials-management/refdata'(resources: 'refdata') {
      collection {
        "/$domain/$property" (controller: 'refdata', action: 'lookup', method: 'GET')
      }
    }

    "/serials-management/settings/appSettings" (resources: 'setting');

    "/serials-management/serials" (resources: 'serial')

    "/serials-management/rulesets" (resources: 'serialRuleset'){
      
      '/active' (controller: 'serialRuleset', action: 'activateRuleset', method: 'POST')
      '/deprecated' (controller: 'serialRuleset', action: 'deprecateRuleset', method: 'POST')
      '/draft' (controller: 'serialRuleset', action: 'draftRuleset', method: 'POST')
    }

    "/serials-management/predictedPieces" (resources: 'predictedPieceSet') {
      collection {
        "/generate" (controller: 'predictedPieceSet', action: 'generatePredictedPiecesTransient')
        "/create" (controller: 'predictedPieceSet', action: 'generatePredictedPieces')
      }
    }


    
  }
}
