package org.olf.combination

import org.olf.combination.combinationPattern.*

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class CombinationRule implements MultiTenant<CombinationRule> {
  String id
  Combination owner

  Integer issuesToCombine

  @CategoryId(value="CombinationRule.TimeUnits", defaultInternal=true)
  @Defaults(['Day', 'Week', 'Month', 'Issue'])
  RefdataValue timeUnit 

  @CategoryId(value="CombinationRule.PatternType", defaultInternal=true)
  @Defaults(['Day', 'Day Month', 'Day Week', 'Day Weekday', 'Day Week Month', 'Week', 'Week Month', 'Month', 'Issue', 'Issue Week', 'Issue Week Month', 'Issue Month'])
  RefdataValue patternType

  @BindUsing({ CombinationRule obj, SimpleMapDataBindingSource source ->
		CombinationRuleHelpers.doRulePatternBinding(obj, source)
  })
  CombinationPattern pattern

	static belongsTo = [
    owner : Combination
  ]

	static hasOne = [
   	pattern: CombinationPattern
  ]

	static mapping = {
       	  	     id column: 'cr_id', generator: 'uuid2', length: 36
     	      	owner column: 'cr_owner_fk'
            version column: 'cr_version'
           timeUnit column: 'cr_time_unit'
    issuesToCombine column: 'cr_issues_to_combine'
        patternType column: 'cr_pattern_type_fk'
		       pattern cascade: 'all-delete-orphan'
  }

  static constraints = {
              owner nullable: false
    issuesToCombine nullable: false
        patternType nullable: false
           timeUnit nullable: false
            pattern nullable: false, validator: CombinationRuleHelpers.rulePatternValidator
  }
}
