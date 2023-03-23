package org.olf.recurrence

import org.grails.datastore.mapping.dirty.checking.DirtyCheckable
import org.olf.recurrence.recurrencePattern.*

import grails.gorm.MultiTenant
import grails.databinding.BindUsing
import grails.databinding.SimpleMapDataBindingSource

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import org.springframework.validation.ObjectError

import java.util.regex.Pattern

public class RecurrenceRule implements MultiTenant<RecurrenceRule> {
  String id
  Recurrence owner
  Integer ordinal /* Validated to be 1 - period of owner. Default is 1 */

  @CategoryId(value="RecurrenceRule.PatternType", defaultInternal=true)
  @Defaults(['Day', 'Week', 'Month Date', 'Month Weekday', 'Year Date', 'Year Weekday', 'Year Month Weekday'])
  RefdataValue patternType

  @BindUsing({ RecurrenceRule obj, SimpleMapDataBindingSource source ->
		RecurrenceRuleHelpers.doRulePatternBinding(obj, source)
  })
  RecurrencePattern pattern // Validate that patternType Year_Weekday -> RecurrencePatternYearWeekday

  /* Day - "" */
  /* Week - Mon/Tue/Wed/Thur/Fri/Sat/Sun */
  /* Month_Date - 1/2/3/../28/-1 */ /* 1/.../31/-1 AND fallback, 29f28 etc OR default any number >28 to fallback to last */
  /* Month_Weekday - 1/2/3/4/-1 + Mon/Tues/.../Sun */
  /* Year_Date - 1/.../31/-1 + Jan/Feb/.../Dec (Validate date against month? (What to do about 29th Feb? - Use last?)) */
  /* Year_Weekday - 1/.../52/-1 + Mon/Tues/.../Sun */
  /* Year_Month_Weekday 1/2/3/4/-1 + Mon/Tues/.../Sun + Jan/Feb/.../Dec */

	static belongsTo = [
    owner : Recurrence
  ]

	static hasOne = [
   	pattern: RecurrencePattern
  ]

	static mapping = {
       	  	 id column: 'rr_id', generator: 'uuid2', length: 36
     	  	'owner' column: 'rr_owner_fk'
        version column: 'rr_version'
        ordinal column: 'rr_ordinal'
    patternType column: 'rr_pattern_type_fk'
		   pattern cascade: 'all-delete-orphan'
  }

  static constraints = {
          'owner' nullable: false
        ordinal nullable: false
    patternType nullable: false
        pattern nullable: false, validator: RecurrenceRuleHelpers.rulePatternValidator
  }
}
