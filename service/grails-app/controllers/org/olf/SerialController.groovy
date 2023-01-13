package org.olf

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.Serial
import groovy.util.logging.Slf4j

class SerialController extends OkapiTenantAwareController<Serial> {

  SerialController(){
    super(Serial)
  }

}
