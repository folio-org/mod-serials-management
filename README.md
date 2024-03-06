# mod-serials-management

Copyright (C) 2024 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

# Notes on third party libraries used in this software
* This module uses [logback-groovy-config](https://github.com/virtualdogbert/logback-groovy-config) under an Eclipse Public License (EPL) v1.0.
  * Developed by [Tucker Pelletier](https://github.com/virtualdogbert)
  * Code: https://github.com/virtualdogbert/logback-groovy-config
  * Documentation: https://virtualdogbert.github.io/logback-groovy-config/
  * License: https://github.com/virtualdogbert/logback-groovy-config/blob/main/EPL.txt

# Introduction

mod-serials-management is a FOLIO module to support serials management functionality. Specifically this includes managing serial records which support a status and description, can be linked to a purchase order line and title from mod-orders and can have a publication pattern defined.

Publication patterns (called `rulesets` in the code) specify information required to predict what issues are expected to be published for a serial during a given time period, and can be used with serials records to generate a list of expected receiving pieces in mod-orders.

### Code of Conduct

Refer to the Wiki [FOLIO Code of Conduct](https://wiki.folio.org/display/COMMUNITY/FOLIO+Code+of+Conduct).

## Module installation and upgrade notes

The module has important dependences on reference data. initial installations and module upgrades should specify loadReference=true. The module may not work as expected if this is omitted.

The following reference data (refdata) categories are created on installation:

| Refdata Category | URL for values |
| --- | --- |
|ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat|/serials-management/refdata/ChronologyTemplateMetadataRule/TemplateMetadataRuleFormat|
|CombinationRule.PatternType|/serials-management/refdata/CombinationRule/PatternType|
|CombinationRule.TimeUnits|/serials-management/refdata/CombinationRule/TimeUnits|
|EnumerationNumericLevelTMRF.Format|/serials-management/refdata/EnumerationNumericLevelTMRF/Format|
|EnumerationNumericLevelTMRF.Sequence|/serials-management/refdata/EnumerationNumericLevelTMRF/Sequence|
|EnumerationTemplateMetadataRule.TemplateMetadataRuleFormat|/serials-management/refdata/EnumerationTemplateMetadataRule/TemplateMetadataRuleFormat|
|Global.Month|/serials-management/refdata/Global/Month|
|Global.MonthDayFormat|/serials-management/refdata/Global/MonthDayFormat|
|Global.MonthFormat|/serials-management/refdata/Global/MonthFormat|
|Global.Weekday|/serials-management/refdata/Global/Weekday|
|Global.WeekdayFormat|/serials-management/refdata/Global/WeekdayFormat|
|Global.YearFormat|/serials-management/refdata/Global/YearFormat|
|OmissionRule.PatternType|/serials-management/refdata/OmissionRule/PatternType|
|OmissionRule.TimeUnits|/serials-management/refdata/OmissionRule/TimeUnits|
|Recurrence.TimeUnits|/serials-management/refdata/Recurrence/TimeUnits|
|RecurrenceRule.PatternType|/serials-management/refdata/RecurrenceRule/PatternType|
|Serial.SerialStatus|/serials-management/refdata/Serial/SerialStatus|
|SerialRuleset.RulesetStatus|/serials-management/refdata/SerialRuleset/RulesetStatus|
|TemplateMetadataRule.TemplateMetadataRuleType|/serials-management/refdata/TemplateMetadataRule/TemplateMetadataRuleType|

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

## ModuleDescriptor

https://github.com/folio-org/mod-serials-management/blob/master/service/src/main/okapi/ModuleDescriptor-template.json

## Additional information

### Issue tracker

See project [mod-serials-management](https://folio-org.atlassian.net/browse/MODSER)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker/).

### Other documentation

Other [modules](https://dev.folio.org/source-code/#server-side) are described,
with further FOLIO Developer documentation at [dev.folio.org](https://dev.folio.org/)

### Download and configuration

The built artifacts for this module are available.
See [configuration](https://dev.folio.org/download/artifacts) for repository access,
and the [Docker image](https://hub.docker.com/r/folioci/mod-serials-management).

Notes on github actions for grails: https://guides.grails.org/grails-on-github-actions/guide/index.html, https://dev.to/erichelgeson/grails-ci-with-github-actions-25ff

## Running using grails run-app with the vagrant-db profile

    grails -Dgrails.env=vagrant-db run-app


## Initial Setup

Most developers will run some variant of the following commands the first time through

### In window #1

Start the vagrant image up from the project root

    vagrant destroy
    vagrant up

Sometimes okapi does not start cleanly in the vagrant image - you can check this with

    vagrant ssh

then once logged in

    docker ps

should list running images - if no processes are listed, you will need to restart okapi (In the vagrant image) with

    sudo su - root
    service okapi stop
    service okapi start

Finish the part off with

    tail -f /var/log/folio/okapi/okapi.log

### In window #2

Build and run mod-agreements stand alone

    cd service
    grails war
    ../scripts/run_external_reg.sh
