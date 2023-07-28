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

    "/serials-management/rulesets" (resources: 'serialRuleset')

    "/serials-management/predictedPieces/generate" (controller: 'predictedPieces', action: 'generatePredictedPiecesTransient')
    "/serials-management/predictedPieces/create" (controller: 'predictedPieces', action: 'generatePredictedPieces')
  }
}
