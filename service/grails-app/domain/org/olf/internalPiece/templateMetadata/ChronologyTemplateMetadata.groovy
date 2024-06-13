package org.olf.internalPiece.templateMetadata

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyTemplateMetadata extends TemplateMetadataFormat implements MultiTenant<ChronologyTemplateMetadata> {

  String weekday
  String monthDay
  String month
  String year

  static constraint = {
    weekday nullable: true
    monthDay nullable: true
    month nullable: true
    year nullable: true
  }

  static mapping = {
    weekday column: 'ctm_weekday'
    monthDay column: 'ctm_monthDay'
    month column: 'ctm_month'
    year column: 'ctm_year'
  }
}
