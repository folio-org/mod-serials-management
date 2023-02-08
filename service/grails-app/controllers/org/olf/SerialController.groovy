package org.olf

import org.olf.Serial
import org.olf.recurrence.recurrencePattern.RecurrencePattern

import com.k_int.okapi.OkapiTenantAwareController

import grails.rest.*
import grails.converters.*
import grails.gorm.transactions.Transactional
import grails.gorm.multitenancy.CurrentTenant
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

@Slf4j
@CurrentTenant
class SerialController extends OkapiTenantAwareController<Serial> {

  SerialController(){
    super(Serial)
  }

  @Transactional
  def doPost () {
    def objToBind = getObjectToBind();
    final Class<? extends RecurrencePattern> rc = Class.forName("org.olf.recurrence.recurrencePattern.RecurrencePatternMonthWeekday")
    RecurrencePattern rp = rc.newInstance()
    // log.debug(rp)

    bindData(rp, objToBind)
    rp.save()
 }

}
