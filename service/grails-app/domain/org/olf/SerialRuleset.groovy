package org.olf

import org.olf.recurrence.Recurrence
import org.olf.omission.Omission
import org.olf.combination.Combination
import org.olf.templateConfig.TemplateConfig

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import groovy.sql.Sql


@GrailsCompileStatic
class SerialRuleset implements MultiTenant<SerialRuleset> {

  String id
  Date lastUpdated
  Date dateCreated

  String rulesetNumber
  String description

  @CategoryId(defaultInternal=true)
  @Defaults(['Active', 'Draft', 'Deprecated'])
  RefdataValue rulesetStatus

  static hasOne = [
    recurrence: Recurrence,
    omission: Omission,
    combination: Combination,
    templateConfig: TemplateConfig
  ]

  static mappedBy = [
    recurrence: 'owner',
    omission: 'owner',
    combination: 'owner',
    templateConfig: 'owner'
  ]

  static belongsTo = [
    owner: Serial
  ]

  static mapping = {
    id column: 'sr_id', generator: 'uuid2', length: 36
    lastUpdated column: 'sr_last_updated'
    dateCreated column: 'sr_date_created'
    owner column: 'sr_owner_fk'
    version column: 'sr_version'
    rulesetNumber column: 'sr_ruleset_number'
    description column: 'sr_description'
    rulesetStatus column: 'sr_ruleset_status_fk'

    recurrence cascade: 'all-delete-orphan'
    omission cascade: 'all-delete-orphan'
    combination cascade: 'all-delete-orphan'
    templateConfig cascade: 'all-delete-orphan'
  }
  
  static constraints = {
    lastUpdated nullable: true
    dateCreated nullable: true
    owner nullable: false
    rulesetNumber nullable: true
    rulesetStatus nullable: false
    description nullable: true
    recurrence nullable: true
    omission nullable: true
    combination nullable: true
    templateConfig nullable: true
  }   
}
