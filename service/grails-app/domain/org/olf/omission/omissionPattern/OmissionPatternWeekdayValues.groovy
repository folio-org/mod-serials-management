package org.olf.omission.omissionPattern

import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.refdata.CategoryId

public class OmissionPatternWeekdayValues implements MultiTenant<OmissionPatternWeekdayValues> {

	String id

  @CategoryId(value="Global.Weekday", defaultInternal=true)
  @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
  RefdataValue weekday

	static belongsTo = [ owner: OmissionPattern ]

	  static mapping = {
           id column: 'opwdv_id', generator: 'uuid2', length:36
      version column: 'opwdv_version'
      weekday column: 'opwdv_weekday_fk'
        owner column: 'opwdv_owner_fk'
	}

	static constraints = {
		 owner nullable:false
	   weekday nullable:false
	}

}