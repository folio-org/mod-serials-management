package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyUCTMT extends UserConfiguredTemplateMetadataType implements MultiTenant<ChronologyUCTMT> {

  String weekday
  String monthDay
  String month
  String year

  static constraints = {
    weekday nullable: true
    monthDay nullable: true
    month nullable: true
    year nullable: true
  }

  static mapping = {
    weekday column: 'cuctmt_weekday'
    monthDay column: 'cuctmt_month_day'
    month column: 'cuctmt_month'
    year column: 'cuctmt_year'
  }
}
