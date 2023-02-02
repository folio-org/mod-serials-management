package org.olf

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class RecurrenceRule implements MultiTenant<RecurrenceRule> {
  String id
  Recurrence owner
  Integer ordinal /* Validated to be 1 - period of owner. Default is 1 */

  @CategoryId(value="RecurrenceRule.PatternType", defaultInternal=true)
  @Defaults(['Day', 'Week', 'Month Date', 'Month Weekday', 'Year Date', 'Year Weekday', 'Year Month Weekday'])
  RefdataValue patternType
    
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
       	  	 id column: 'rer_id', generator: 'uuid2', length: 36
     	  	owner column: 'rer_owner_fk'
        ordinal column: 'rer_ordinal'
    patternType column: 'rer_pattern_type_fk'
		   pattern cascade: 'all-delete-orphan'
  }
}
