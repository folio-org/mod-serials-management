package org.olf.omission

import org.olf.omission.omissionPattern.*

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class OmissionRule implements MultiTenant<OmissionRule> {
  String id
  Omission owner

  @CategoryId(value="OmissionRule.PatternType", defaultInternal=true)
  @Defaults(['Days in month', 'Weekdays in week', 'Weekdays in month', 'Weeks', 'Weeks in every month', 'Months', 'Nth issue'])
  RefdataValue patternType

  @BindUsing({ OmissionRule obj, SimpleMapDataBindingSource source ->
		OmissionRuleHelpers.doRulePatternBinding(obj, source)
  })
  OmissionPattern pattern

	static belongsTo = [
    owner : Omission
  ]

	static hasOne = [
   	pattern: OmissionPattern
  ]

	static mapping = {
       	  	 id column: 'or_id', generator: 'uuid2', length: 36
     	  	owner column: 'or_owner_fk'
        version column: 'or_version'
    patternType column: 'or_pattern_type_fk'
		   pattern cascade: 'all-delete-orphan'
  }

  static constraints = {
          owner nullable: false
    patternType nullable: false
        pattern nullable: false, validator: OmissionRuleHelpers.rulePatternValidator
  }
}
