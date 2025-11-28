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

### Environment variables
This is a NON-EXHAUSTIVE list of environment variables which tweak behaviour in this module

| Variable                   | Description                                                                                                                                                                                                                                                          | Options                                                    | Default                       |
|----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------|-------------------------------|                               |
| `ENDPOINTS_INCLUDE_STACK_TRACE` | Allows the HTTP response 500 to contain stacktrace from the exception thrown. Default return will be a generic message and a timestamp.                                                                                                                             | <ul><li>`true`</li><li>`false`</li></ul>                                                     | `false`                       |

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

### Issues
This module has a few "problem" scenarios that _shouldn't_ occur in general operation, their history, reasoning and workarounds are documented below.
#### Locks and failure to upgrade
Particular approaches to upgrades in particular can leave the module unable to self right.
This occurs especially often where the module or container die/are killed regularly shortly after/during the upgrade.
The issue documented here was exacerbated by transaction handling changes brought about by the grails 5 -> 6 upgrade as part of Quesnalia, and fix attempts are ongoing.

In order of importance to check:

- **CPU resource**
    - In the past we have had these particular issues reported commonly where the app was not getting enough CPU resources to run. Please ensure that the CPU resources being allocated to the application are sufficient, see the requisite module requirements for the version running ([Ramsons example matrix](https://folio-org.atlassian.net/wiki/spaces/REL/pages/398983244/Ramsons+R2+2024+-+Bugfest+env+preparation+-+Modules+configuration+details?focusedCommentId=608305153))
- **Liquibase**
    - The module uses liquibase in order to facilitate module data migrations
    - Unfortunately this has a weakness to shutdown mid migration.
    - Check `<tenantName>_mod_serials_management.tenant_changelog_lock` does not have `locked` set to `true`
        - If it does, the migration (and hence the upgrade itself) have failed, and it is difficult to extricate the module from this scenario.
        - It may be most prudent to revert the data and retry the upgrade.
    - In general, while the module is uploading it is most likely to succeed if after startup and tenant enabling/upgrading through okapi that the module and its container are NOT KILLED for at least 2 minutes.
    - An addition, a death to the module while upgrading could be due to a lack of reasonable resourcing making it to the module
- **Federated changelog lock**
    - The module also has a manual lock which is managed by the application itself.
    - This is to facilitate multiple instances accessing the same data
    - In particular, this lock table "seeds" every 20 minutes or so, and a death in the middle of this _can_ lock up the application (Although it can sometimes self right from here)
    - If the liquibase lock is clear, first try startup and leaving for a good 20 minutes
        - If the module dies it's likely resourcing that's the issue
        - The module may be able to self right
    - If the module cannot self right
        - Check the `mod_serials_management__system.system_changelog_lock`
            - The same applies from the above section as this is a liquibase lock, but this is seriously unlikely to get caught as the table is so small
        - Finally check the `mod_serials_management__system.federation_lock`
            - If this table has entries, this can prevent the module from any and all operations
            - It should self right from here, even if pointing at dead instances
                - See `mod_serials_management__system.app_instance` for a table of instance ids, a killed and restarted module should eventually get cleared from here.
                - It is NOT RECOMMENDED to clear app_instances manually
            - If there are entries in the federated lock table that do not clear after 20 minutes of uninterrupted running then this table should be manually emptied.

#### Connection pool issues
As of Sunflower release, issues with [federated locks](#locks-and-failure-to-upgrade) and connection pools have been ongoing since Quesnalia.
The attempted fixes and history is documented in JIRA ticket [ERM-3851](https://folio-org.atlassian.net/browse/ERM-3851)

Initially the Grails 6 upgrade caused federated lock rows to themselves lock in PG.
A fix was made for Sunflower (v2.0.0) and backported to Quesnalia (v1.0.5) and Ramsons (v1.1.6).
However this fix is both not fully complete, and worsens an underlying connection pool issue.

The connection pool per instance can be configured via the `DB_MAXPOOLSIZE` environment variable.
Since the introduction of module-federation for this module, this has been _doubled_ to ensure connections are available
for the system schema as well. This is necessary as a starved system schema would all but guarantee the fed lock issues
documented above. As a response, our approach was to request more and more connections, memory, and CPU time to lower the
chances of this happening as much as possible.

As of right now, the recommended Sunflower connection pool is 10 per instance.
This leads to 20 connections per instance, almost all of which PG will see as idle. The non-dropping of idle connections
is a [chosen behaviour of Hikari](https://www.postgresql.org/message-id/1395487594923-5797135.post@n5.nabble.com) (and so not a bug).

At the moment, although postgres sees most of this pool as idle, Hikari internally believes them to be active, causing
pool starvation unless massively over-resourcing the instance. This in turn locks up the instance entirely and leads to jobs
silently failing

The workarounds here are to over-resource the module, and to restart problematic instances (or all instances)
when this behaviour manifests, or to revert to versions where this is less prevalent (v6.1.3, v6.2.0) and handle the
federated locking issues instead. Obviously these are not proper solutions.

In Trillium (v2.1.0), the aim is both to fix these bugs, and hopefully thus free up the connection pool to an extent that it can be
run with _significantly_ fewer connections, and potentially set up a way for the configured pool size to be mathematically
split between system and module, so as to avoid the doubling of the pool.

The recommendation for the versions containing the fix is to run with a minimum of 10 connections per instance
(Which will be doubled to 20 to account for the system schema).

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
