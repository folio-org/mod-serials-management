package org.olf.recurrence

import org.olf.SerialRuleset

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class Recurrence implements MultiTenant<Recurrence> {
  String id
  SerialRuleset owner

  @CategoryId(value="Recurrence.TimeUnits", defaultInternal=true)
  @Defaults(['Day', 'Week', 'Month', 'Year'])
  RefdataValue timeUnit /* defaults Day/Week/Month/Year*/

  Integer issues // 7
  Integer period // 3 / Frequency

  Set<RecurrenceRule> rules// Validate to have exactly #issues of these

  static belongsTo = [
    owner: SerialRuleset
 	]

  static hasMany = [
    rules : RecurrenceRule
  ]

  static mapping = {
          id column: 'r_id', generator: 'uuid2', length: 36
       owner column: 'r_owner_fk'
     version column: 'r_version'
    timeUnit column: 'r_time_unit_fk'
      issues column: 'r_issues'
      period column: 'r_period'
      rules cascade: 'all-delete-orphan'
  }

    static constraints = {
            id nullable: false
         owner nullable: false
      timeUnit nullable: false
        issues nullable: false
        period nullable: false
         rules nullable: false
    }
}

// Example Input
// Issues 6, period every 2, timeUnit Month
// UI on day 1 of month 2
// Rule Ordinal 2 patternType/Pattern month_date
// Month_date 1
