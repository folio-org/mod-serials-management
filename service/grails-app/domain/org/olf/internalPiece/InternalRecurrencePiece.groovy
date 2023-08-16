package org.olf.internalPiece

import org.olf.recurrence.RecurrenceRule

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class InternalRecurrencePiece extends InternalPiece implements MultiTenant<InternalRecurrencePiece> {

  LocalDate date

  RecurrenceRule recurrenceRule

  static mapping = {
    date column: 'irp_date'
    recurrenceRule column: 'irp_recurrence_rule_fk'
  }

  static constraint = {
    recurrenceRule nullable: true
  }
}