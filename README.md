# mod-serials-management

Copyright (C) 2022 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

# Introduction

FOLIO Backend module for Serials Management

### Code of Conduct

Refer to the Wiki [FOLIO Code of Conduct](https://wiki.folio.org/display/COMMUNITY/FOLIO+Code+of+Conduct).

### Deployment

A sample k8s resource definition for service and deployment [can be found the scripts directory](https://github.com/folio-org/mod-serials-management/blob/master/scripts/k8s_deployment_template.yaml)
Or you can get the latest module descriptor from the project OKAPI - [For example - v1.0.0-SNAPSHOT65](curl http://folio-registry.aws.indexdata.com/_/proxy/modules/mod-serials-management-1.0.0-SNAPSHOT.65)

This module requires the following env parameters
* OKAPI_SERVICE_PORT - port number for okapi
* OKAPI_SERVICE_HOST - Host [namespace.hostname if running in a different namespace to okapi]

The following properties are understood and documented in the [Module Descriptor](https://github.com/folio-org/mod-serials-management/blob/master/service/src/main/okapi/ModuleDescriptor-template.json)
* DB_DATABASE
* DB_HOST
* DB_USERNAME
* DB_PASWORD
* DB_MAXPOOLSIZE
* DB_PORT

### Other documentation

Other [modules](https://dev.folio.org/source-code/#server-side) are described,
with further FOLIO Developer documentation at [dev.folio.org](https://dev.folio.org/)

### Issue tracker

See project [MODSER](https://issues.folio.org/browse/MODSER)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker).

### Download and configuration

The built artifacts for this module are available.
See [configuration](https://dev.folio.org/download/artifacts) for repository access,
and the [Docker image](https://hub.docker.com/r/folioci/mod-serials-management).



Notes on github actions for grails: https://guides.grails.org/grails-on-github-actions/guide/index.html, https://dev.to/erichelgeson/grails-ci-with-github-actions-25ff
