## 1.1.7 2025-12-17
  * ERM-3851 Long standing connection issues bug
  * ERM-3292 Stack traces should not be included in API responses

## 1.1.6 2025-04-03
  * MODSER-111 Prevent instability of grails modules during updates: Ramsons

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
