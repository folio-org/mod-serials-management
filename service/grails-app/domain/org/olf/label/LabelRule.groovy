package org.olf.label

import org.olf.label.labelStyle.*

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class LabelRule implements MultiTenant<LabelRule> {
  String id
  Label owner

  @CategoryId(value="LabelRule.LabelStyles", defaultInternal=true)
  @Defaults(['Chronology', 'Enumeration'])
  RefdataValue labelStyle 

  @BindUsing({ LabelRule obj, SimpleMapDataBindingSource source ->
		LabelRuleHelpers.doRuleStyleBinding(obj, source)
  })
  LabelStyle style

	static belongsTo = [
    owner : Label
  ]

	static hasOne = [
   	style: LabelStyle
  ]

	static mapping = {
       	 	     id column: 'lr_id', generator: 'uuid2', length: 36
     	   	  owner column: 'lr_owner_fk'
          version column: 'lr_version'
       labelStyle column: 'lr_label_style_fk'
	  	     style cascade: 'all-delete-orphan'
  }

  static constraints = {
            owner nullable: false
       labelStyle nullable: false
            style nullable: false, validator: LabelRuleHelpers.ruleStyleValidator
  }
}
