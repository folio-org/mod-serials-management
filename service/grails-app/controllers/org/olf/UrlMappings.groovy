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
  }
}
