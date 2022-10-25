package org.olf

import com.k_int.okapi.OkapiHeaders
import com.k_int.web.toolkit.testing.HttpSpec

import grails.testing.mixin.integration.Integration
import groovyx.net.http.FromServer
import spock.lang.*
import spock.util.concurrent.PollingConditions
import groovy.util.logging.Slf4j

/**
 * This class requires special properties to be configured in grails-app/config/application-test.yml - this file
 * is in the .gitignore file to ensure we do not leak API keys via git. See the .gitignore file for details about the
 * properties that need to be set
 */

@Slf4j
@Integration
@Stepwise
class TestSpec extends HttpSpec {

  static final String tenantName = 'pr_tests'

  @Shared
  def grailsApplication

  static final Closure booleanResponder = {
    response.success { FromServer fs, Object body ->
      true
    }
    response.failure { FromServer fs, Object body ->
      false
    }
  }

  def setupSpec() {
    httpClientConfig = {
      client.clientCustomizer { HttpURLConnection conn ->
        conn.connectTimeout = 2000
        conn.readTimeout = 10000 // Need this for activating tenants
      }
    }
  }

  def setup() {
    setHeaders((OkapiHeaders.TENANT): tenantName)
  }


  void "Create Tenant" () {
    when: 'Create the tenant'
      boolean resp = doPost('/_/tenant', {
        parameters ([["key": "loadReference", "value": "true" ],
                     ["key": "loadSample", "value": "true" ] ])
      }, null, booleanResponder);
    then:
      resp == true
  }

}
