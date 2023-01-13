package org.olf

import grails.gorm.MultiTenant

import java.time.LocalDate
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue
import groovy.sql.Sql
import com.k_int.web.toolkit.settings.AppSetting
import com.k_int.okapi.remote_resources.RemoteOkapiLink

class SerialOrderLine extends RemoteOkapiLink implements MultiTenant<SerialOrderLine> {

  static belongsTo = [ owner: Serial ]

  static mapping = {
    owner column: 'sol_owner'
  }

  static constraints = {
    owner(nullable:false, blank:false)
  }

  @Override
  public final def remoteUri() {
    {->
      "orders/order-lines/${remoteId}"
    }
  }

}
