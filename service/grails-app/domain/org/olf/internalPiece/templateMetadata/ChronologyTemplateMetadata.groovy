package org.olf.internalPiece.templateMetadata

import org.olf.templateConfig.templateMetadataRule.TemplateMetadataRule

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class ChronologyTemplateMetadata extends TemplateMetadata implements MultiTenant<ChronologyTemplateMetadata> {

  String weekday
  String monthDay
  String month
  String year

  TemplateMetadataRule templateMetadataRule

  static constraint = {
    weekday nullable: true
    monthDay nullable: true
    month nullable: true
    year nullable: true
    templateMetadataRule nullable: true  
  }
}
