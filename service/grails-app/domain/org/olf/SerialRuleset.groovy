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

// TODO With the addition of a RulesetOwner super class, this should be renamed to just Ruleset (or something similar)
@GrailsCompileStatic
class SerialRuleset implements MultiTenant<SerialRuleset> {

  String id
  Date lastUpdated
  Date dateCreated

  String rulesetNumber
  String description

  RulesetOwner owner

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
    owner nullable: true
    rulesetNumber nullable: true
    rulesetStatus nullable: false
    description nullable: true
    recurrence nullable: true
    omission nullable: true
    combination nullable: true
    templateConfig nullable: true
  } 

  def beforeValidate() {
    if ( rulesetNumber == null ) {
      this.rulesetNumber = generateHrid()
    }
  }

  private String generateHrid() {
    String result = null;

    // Use this to make sessionFactory.currentSession work as expected
    SerialRuleset.withSession { SessionImpl session ->
      log.debug("Generate hrid");
      def sql = new Sql(session.connection())
      def query_result = sql.rows("select nextval('serial_ruleset_hrid_seq')".toString())
      log.debug("Query result: ${query_result.toString()}")
      result = query_result[0].get('nextval')?.toString()
    }
    return result;
  }
}
