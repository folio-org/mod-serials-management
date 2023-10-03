package org.olf.label.labelStyle

import org.olf.label.labelFormat.LabelFormat

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class LabelStyleChronology extends LabelStyle implements MultiTenant<LabelStyleChronology> {

  @CategoryId(value="LabelStyleChronology.LabelFormat", defaultInternal=true)
  @Defaults(['Chronology Date', 'Chronology Month', 'Chronology Year'])
  RefdataValue labelFormat

  @BindUsing({ LabelStyle obj, SimpleMapDataBindingSource source ->
		LabelStyleHelpers.doStyleFormatBinding(obj, source)
  })
  LabelFormat format

  static hasOne = [
   	format: LabelFormat
  ]


  static mapping = {
      labelFormat column: 'lsc_label_format_fk'
      	  format cascade: 'all-delete-orphan'
  }

  static constraints = {
      labelFormat nullable: false
      format nullable: false, validator: LabelStyleHelpers.styleFormatValidator
  }
}
