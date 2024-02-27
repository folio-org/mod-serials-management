package org.olf

import com.k_int.okapi.OkapiHeaders
import com.k_int.web.toolkit.testing.HttpSpec

import groovyx.net.http.HttpException
import spock.lang.Stepwise
import spock.util.concurrent.PollingConditions

import groovy.util.logging.Slf4j

@Slf4j
@Stepwise
abstract class BaseSpec extends HttpSpec {

  def setupSpec() {
    httpClientConfig = {
      client.clientCustomizer { HttpURLConnection conn ->
        conn.connectTimeout = 120000
        conn.readTimeout = 60000
      }
    }
    addDefaultHeaders(
      (OkapiHeaders.TENANT): "${this.class.simpleName}",
      (OkapiHeaders.USER_ID): "${this.class.simpleName}_user"
    ) 
  }
  
  Map<String, String> getAllHeaders() {
    state.specDefaultHeaders + headersOverride
  }
  
  String getCurrentTenant() {
    allHeaders?.get(OkapiHeaders.TENANT)
  }
  
  void 'Pre purge tenant' () {
    boolean resp = false

    println("test");
    log.debug("Pre purge tenant");

    when: 'Purge the tenant'
      try {
        resp = doDelete('/_/tenant', null)
        resp = true
      } catch (HttpException ex) { resp = true }
      
    then: 'Response obtained'
      resp == true
  }
  
  void 'Ensure test tenant' () {

		log.debug("Ensure test tenant ${baseUrl}");
    
    when: 'Create the tenant'
      def resp = doPost('/_/tenant', {
      parameters ([["key": "loadReference", "value": true]])
    })

    then: 'Response obtained'
    resp != null

    and: 'Refdata added'

      List list
      // Wait for the refdata to be loaded.
      def conditions = new PollingConditions(timeout: 10)
      conditions.eventually {
        (list = doGet('/serials-management/refdata')).size() > 0
      }
  }
}
