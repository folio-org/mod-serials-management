## 2.1.0 IN PROGRESS
  * MODSER-110: Migrated pattern (Ramsons -> Sunflower) with combination generates incorrect labels for predicted pieces

## 2.0.4 2025-11-28
  * ERM-3851 Long standing connection issues bug
  * ERM-3292 Stack traces should not be included in API responses

## 2.0.3 2025-04-28
  * MODSER-118 Fixed Chronology/Enumeration template metadata rule primary key

## 2.0.2 2025-04-15
  * MODSER-115 Upgrade undertow from 2.2.28.Final to 2.2.37.Final fixing vulns
  * MODSER-114 kafka-clients 3.7.2, jackson 2.18.3 fixing vulns
  * MODSER-110 Migrated pattern (Ramsons -> Sunflower) with combination generates incorrect labels for predicted pieces
  * MODSER-104 Passing a refdata category desc to an enumeration textual rule causes a new refdata category to be created


## 2.0.1 2025-03-28
  * MODSER-111 Patch mod-serials-management for instability during update
  * MODSER-109 Error on attempting to preview/generate pieces for publication pattern after Ramsons->Sunflower migration
  * MODSER-105 Attempting to create a ModelRuleset with an owner causes an error
  * MODSER-103 Error when attempting to create a piece set with a combined last piece
  * Improved integration test coverage

## 2.0.0 2025-03-13
  * MODSER-101 Combined enumeration does not skip the combined issue numbers
  * MODSER-99 Update data binding to support the use of ref data values in textual enumeration
  * MODSER-97 Update Serials API documentation
  * MODSER-83 Implement integration tests workflow for Github Actions
  * MODSER-71 continuationPieceRecurrenceMetadata stores incorrect enumeration rawValues/values
  * MODSER-68 Add backend support for publication pattern templates
  * MODSER-42 Separate out chronology and enumeration
  * Removed old starting value shape support

## 1.1.5 2025-02-04
  * MODSER-90 Migration issue with module mod-serials-management

## 1.1.4 2025-02-03
  * MODSER-88 Mod-serials-management fails to migrate from 1.0.3 to 1.1.2 due to liquibase errors

## 1.1.3 2025-01-24
  * MODSER-84 Add missing required interfaces to Module Descriptor

## 1.1.2 2025-01-09
  * MODSER-70 Update API documentation for preview/generate predicted pieces

## 1.1.1 2024-11-28
  * MODSER-62 Multiple levels of continuous enumeration in a publication pattern result in incorrect numbering
  * MODSER-60 Receiving piece count not displaying in predicted piece set

## 1.1.0 2024-10-30
  * MODSER-58 Generating template metadata for internal omitted piece
  * MODSER-57 Generate/create piece set endpoints to return same objects
  * MODSER-56 Internal omission/combination pieces not expanding fields
  * MODSER-50 Update module license, guidance and dependencies for mod-serials-management
  * MODSER-43 Review and cleanup Module Descriptors for mod-serials-management (Eureka)
  * MODSER-37 Prepare backend to support UISER-91 (On generating predicted pieces, if the active ruleset has previously been used, then the form should populate with the next set of starting values)
  * MODSER-35 Endpoints should be protected by fine-grained permissions (Eureka)
  * MODSER-32 Change default labels for chronology format and enumeration format reference data values
  * UISER-153 Support edit for publication patterns
  * Separate out okapi interface version from module version

## 1.0.3 2024-04-26
  * MODSER-35 Endpoints should be protected by fine-grained permissions

## 1.0.1 2024-04-17
  * MODSER-34 Template does not always get data from labels or levels in the same order
  * MODSER-28 Update outdated/vulnerable dependencies
  * UISER-99 Level 1 number not used correctly in predicted piece preview from Create Publication Pattern
  * ERM-3190 DB Connections are not being released

## 1.0.0 2024-03-22
  * Initial release for module serials-management
  * Manage objects relating to the concept of a "serial"
  * Manage rulesets governing the publication behaviour of a serial
  * Generation of predicted pieces from serial ruleset
    * Local internal piece management
    * Push internal pieces into receiving module
  * JIRA Epic:
    * UXPROD-4437	Initial support for serials including prediction patterns and issue generation
  * JIRA Feature Tickets:
    * UXPROD-4386	Serial publication pattern: basic creation, view
    * UXPROD-4385	Support Enumeration and Chronology for serials management
    * UXPROD-4383	Support for specifying issue frequency for serials
    * UXPROD-4382	Support for a 'serial' record
    * UXPROD-4378	Create receiving pieces for predicted serial pieces
    * UXPROD-4352	Create predicted pieces
    * UXPROD-4349	Serial publication patterns: support issue omissions and combinations
