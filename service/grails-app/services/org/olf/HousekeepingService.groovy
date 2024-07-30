package org.olf

import java.sql.ResultSet
import java.util.regex.Pattern

import javax.sql.DataSource

import org.grails.datastore.mapping.core.exceptions.ConfigurationException
import org.grails.orm.hibernate.HibernateDatastore
import org.grails.plugins.databasemigration.liquibase.GrailsLiquibase

import org.olf.internalPiece.templateMetadata.EnumerationLevelUCTMT

import grails.core.GrailsApplication
import grails.events.annotation.Subscriber
import grails.gorm.multitenancy.Tenants
import grails.gorm.transactions.Transactional
import groovy.sql.Sql
import com.k_int.okapi.OkapiTenantAdminService
import com.k_int.web.toolkit.settings.AppSetting
import com.k_int.web.toolkit.refdata.*
import com.k_int.okapi.OkapiTenantResolver

import com.github.fracpete.romannumerals4j.RomanNumeralFormat;



/**
 * This service works at the module level, it's often called without a tenant context.
 */
class HousekeepingService {


  /**
   * This is called by the eventing mechanism - There is no web request context
   * this method is called after the schema for a tenant is updated.
   */
  @Subscriber('okapi:schema_update')
  public void onSchemaUpdate(tn, tid) {
    log.debug("HousekeepingService::onSchemaUpdate(${tn},${tid})")
    setupData(tn, tid);
  }

  /**
   * Put calls to estabish any required reference data in here. This method MUST be communtative - IE repeated calls must leave the 
   * system in the same state. It will be called regularly throughout the lifecycle of a project. It is common to see calls to
   * lookupOrCreate, or "upsert" type functions in here."
   */

  private void cleanupEnumerationLevelMetadata() {
    RomanNumeralFormat formatter = new RomanNumeralFormat();

    Pattern romanRegex = Pattern.compile("(?=.*I)|(?=.*M)|(?=.*C)|(?=.*D)|(?=.*L)|(?=.*X)|(?=.*V)")
    Pattern oridnalRegex = Pattern.compile("(?=.*st)|(?=.*nd)|(?=.*rd)|(?=.*th)")

    // Find all EnumerationLevelUCTMT of all format with missing rawValue/valueFormat
    List<EnumerationLevelUCTMT> incompleteLevels = EnumerationLevelUCTMT.executeQuery('''
      select eluctmt
      from EnumerationLevelUCTMT as eluctmt
      where (eluctmt.rawValue is null)
      or (eluctmt.valueFormat is null)
    ''').each { level ->

      // For each of the found EnumerationLevelUCTMT, figure out if its value is of a Roman, Ordinal or Number format
      // Deparse into its raw value and assign valueFormat
      if(romanRegex.matcher(level?.value).find()){
        level.rawValue =  formatter.parse(level?.value)
        level.valueFormat = RefdataValue.lookupOrCreate('EnumerationNumericLevelTMRF.Format', 'Roman')

      }else if(oridnalRegex.matcher(level?.value).find()){
        String parsedValue = level?.value.replaceAll(/(?<=\d)(rd|st|nd|th)\b/, '')
        level.rawValue = parsedValue as Integer
        level.valueFormat = RefdataValue.lookupOrCreate('EnumerationNumericLevelTMRF.Format', 'Ordinal')

      }else{
        level.rawValue = level.value as Integer
        level.valueFormat = RefdataValue.lookupOrCreate('EnumerationNumericLevelTMRF.Format', 'Number')
      }
  
      level.save(flush:true, failOnError:true)
    }
  }

  private void setupData(tenantName, tenantId) {
    log.info("HousekeepingService::setupData(${tenantName},${tenantId})");
    // Establish a database session in the context of the activated tenant. You can use GORM domain classes inside the closure
    Tenants.withId(tenantId) {
      AppSetting.withNewTransaction { status ->
        // Setup EnumerationTemplateMetadataRule refdata values
        RefdataValue.lookupOrCreate(
          "EnumerationTemplateMetadataRule.TemplateMetadataRuleFormat",
          "Textual",
          "enumeration_textual",
          true
        )
        RefdataValue.lookupOrCreate(
          "EnumerationTemplateMetadataRule.TemplateMetadataRuleFormat",
          "Numeric",
          "enumeration_numeric",
          true
        )

        // Setup ChronologyTemplateMetadataRule refdata values
        RefdataValue.lookupOrCreate(
          "ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat",
          "Date",
          "chronology_date",
          true
        )
        RefdataValue.lookupOrCreate(
          "ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat",
          "Month",
          "chronology_month",
          true
        )
        RefdataValue.lookupOrCreate(
          "ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat",
          "Year",
          "chronology_year",
          true
        )

        cleanupEnumerationLevelMetadata();
      }
    }
  }

  @Subscriber('okapi:tenant_load_sample')
  public void onTenantLoadSample(final String tenantId, 
                                 final String value, 
                                 final boolean existing_tenant, 
                                 final boolean upgrading, 
                                 final String toVersion, 
                                 final String fromVersion) {
    log.debug("HousekeepingService::onTenantLoadSample(${tenantId},${value},${existing_tenant},${upgrading},${toVersion},${fromVersion}");
  }

}
