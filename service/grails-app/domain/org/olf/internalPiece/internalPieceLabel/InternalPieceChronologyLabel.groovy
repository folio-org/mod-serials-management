package org.olf.internalPiece.internalPieceLabel

import org.olf.label.LabelRule

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class InternalPieceChronologyLabel extends InternalPieceLabel implements MultiTenant<InternalPieceChronologyLabel> {

  String weekday
  String monthDay
  String month
  String year

  LabelRule labelRule

  static mapping = {
    weekday column: 'ipcl_weekday'
    monthDay column: 'ipcl_month_day'
    month column: 'ipcl_month'
    year column: 'ipcl_year'
    labelRule column: 'ipcl_label_rule_fk'
  }

  static constraint = {
    weekday nullable: true
    monthDay nullable: true
    month nullable: true
    year nullable: true
    labelRule nullable: true  
  }
}
