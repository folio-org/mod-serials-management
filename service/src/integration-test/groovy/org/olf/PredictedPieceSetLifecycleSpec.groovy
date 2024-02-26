package org.olf

import static groovyx.net.http.ContentTypes.*
import static groovyx.net.http.HttpBuilder.configure
import static org.springframework.http.HttpStatus.*

import com.k_int.okapi.OkapiHeaders
import com.k_int.okapi.OkapiTenantResolver
import geb.spock.GebSpec
import grails.gorm.multitenancy.Tenants
import grails.testing.mixin.integration.Integration
import groovy.json.JsonSlurper
import groovyx.net.http.ChainedHttpConfig
import groovyx.net.http.FromServer
import groovyx.net.http.HttpBuilder
import groovyx.net.http.HttpVerb
import java.time.LocalDate
import spock.lang.Stepwise
import spock.lang.Unroll

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile
import org.springframework.beans.factory.annotation.Value;

import groovy.util.logging.Slf4j

@Slf4j
@Integration
@Stepwise
class PredictedPieceLifecycleSpec extends BaseSpec {
  @Value('${local.server.port}')
  Integer serverPort

  
  void "List Current PredictedPieceSets"() {

    when:"We ask the system to list known PredictedPieceSets"
      List resp = doGet("/serials-management/predictedPieces")

    then: "The system responds with a list of 0"
      resp.size() == 0
  }

  void "Check generating predicted pieces an empty serial with ruleset of month_day recurrence"() {

    when: "Post to create new empty serial with a ruleset"
      log.debug("Create new serial : Empty serial Test");
      Map respMap = doPost("/serials-management/serials", {
        serialStatus 'Active'
        serialRulesets ([{

        rulesetStatus {
          value "active"
        }
        recurrence {
          timeUnit {
            value "month"
          }
          period "1"
          issues "1"
          rules([
            {
              ordinal "1"
              pattern {
                day "1"
              }
              patternType "month_date"
            }
          ])
        }
      templateConfig {
        templateString "Test Template"
      }
      }])
      })
      def serialId = respMap.id

    then: "Response is good and we have a new ID"
      serialId != null
      log.debug("Result of POST: ${respMap}");

    when:"We ask the system to generate PredictedPieceSets"
      List respList = doPost("/serials-management/predictedPieces/generate", {
        rulesetStatus {
          value "active"
        }
        recurrence {
          timeUnit {
            value "month"
          }
          period "1"
          issues "1"
          rules([
            {
              ordinal "1"
              pattern {
                day "1"
              }
              patternType "month_date"
            }
          ])
        }
      templateConfig {
        templateString "Test Template"
      }
      owner {
        id "${serialId}"
      }
      startDate "2023-10-11"
      })

    then: "The system responds with a list of 12"
      respList.size() == 12

  }

}

