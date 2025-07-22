databaseChangeLog = {
  def idPrefix = "20250722-1032"
  def authorName = "mchaib (manual)"

  def migrationsToFix = [
    [ author: authorName, category: 'ModelRuleset.modelRulesetStatus', table: 'model_ruleset',                  fkColumn: 'mr_ruleset_template_status' ],
    [ author: authorName, category: 'Serial.SerialStatus',              table: 'serial',                         fkColumn: 's_serial_status' ],
    [ author: authorName, category: 'SerialRuleset.RulesetStatus',               table: 'serial_ruleset',                 fkColumn: 'sr_ruleset_status_fk' ],
    [ author: authorName, category: 'CombinationRule.TimeUnit',                 table: 'combination_rule',       fkColumn: 'cr_time_unit_fk' ],
    [ author: authorName, category: 'CombinationRule.PatternType',              table: 'combination_rule',       fkColumn: 'cr_pattern_type_fk' ],
    [ author: authorName, category: 'CombinationPatternDayMonth.Month',                    table: 'combination_pattern_day_month',        fkColumn: 'cpdm_month_fk' ],
    [ author: authorName, category: 'CombinationPatternDayWeek.Weekday',                  table: 'combination_pattern_day_week',         fkColumn: 'cpdw_weekday_fk' ],
    [ author: authorName, category: 'CombinationPatternDayWeekMonth.Weekday',                  table: 'combination_pattern_day_week_month',fkColumn: 'cpdwm_weekday_fk' ],
    [ author: authorName, category: 'CombinationPatternDayWeekMonth.Month',                    table: 'combination_pattern_day_week_month',fkColumn: 'cpdwm_month_fk' ],
    [ author: authorName, category: 'CombinationPatternDayWeekday.Weekday',                  table: 'combination_pattern_day_weekday', fkColumn: 'cpdwd_weekday_fk' ],
    [ author: authorName, category: 'CombinationPatternIssueMonth.Month',                    table: 'combination_pattern_issue_month',      fkColumn: 'cpim_month_fk' ],
    [ author: authorName, category: 'CombinationPatternIssueWeekMonth.Month',                    table: 'combination_pattern_issue_week_month', fkColumn: 'cpiwm_month_fk' ],
    [ author: authorName, category: 'CombinationPatternMonth.Month',                    table: 'combination_pattern_month',               fkColumn: 'cpm_month_fk' ],
    [ author: authorName, category: 'CombinationPatternWeekMonth.Month',                    table: 'combination_pattern_week_month',       fkColumn: 'cpwm_month_fk' ],
    [ author: authorName, category: 'EnumerationLevelUCTMT.ValueFormat',              table: 'enumeration_leveluctmt', fkColumn: 'eluctmt_value_format_fk' ],
    [ author: authorName, category: 'UserConfiguredTemplateMetadata.userConfiguredTemplateMetadataType',          table: 'user_configured_template_metadata',           fkColumn: 'uctm_user_configured_template_metadata_type_fk' ],
    [ author: authorName, category: 'OmissionRule.TimeUnit',                 table: 'omission_rule',                       fkColumn: 'or_time_unit_fk' ],
    [ author: authorName, category: 'OmissionRule.PatternType',              table: 'omission_rule',                       fkColumn: 'or_pattern_type_fk' ],
    [ author: authorName, category: 'OmissionPatternDayMonth.Month',                    table: 'omission_pattern_day_month',        fkColumn: 'opdm_month_fk' ],
    [ author: authorName, category: 'OmissionPatternDayWeek.Weekday',                  table: 'omission_pattern_day_week',         fkColumn: 'opdw_weekday_fk' ],
    [ author: authorName, category: 'OmissionPatternDayWeekMonth.Weekday',                  table: 'omission_pattern_day_week_month',fkColumn: 'opdwm_weekday_fk' ],
    [ author: authorName, category: 'OmissionPatternDayWeekMonth.Month',                    table: 'omission_pattern_day_week_month',fkColumn: 'opdwm_month_fk' ],
    [ author: authorName, category: 'OmissionPatternDayWeekday.Weekday',                  table: 'omission_pattern_day_weekday', fkColumn: 'opdwd_weekday_fk' ],
    [ author: authorName, category: 'OmissionPatternIssueMonth.Month',                    table: 'omission_pattern_issue_month',      fkColumn: 'opim_month_fk' ],
    [ author: authorName, category: 'OmissionPatternIssueWeekMonth.Month',                    table: 'omission_pattern_issue_week_month', fkColumn: 'opiwm_month_fk' ],
    [ author: authorName, category: 'OmissionPatternMonth.MonthFrom',                    table: 'omission_pattern_month',               fkColumn: 'opm_month_from_fk' ],
    [ author: authorName, category: 'OmissionPatternMonth.MonthTo',                    table: 'omission_pattern_month',               fkColumn: 'opm_month_to_fk' ],
    [ author: authorName, category: 'OmissionPatternWeekMonth.Month',                    table: 'omission_pattern_week_month',       fkColumn: 'opwm_month_fk' ],
    [ author: authorName, category: 'Recurrence.TimeUnit',                 table: 'recurrence',                fkColumn: 'r_time_unit_fk' ],
    [ author: authorName, category: 'RecurrenceRule.PatternType',              table: 'recurrence_rule',                fkColumn: 'rr_pattern_type_fk' ],
    [ author: authorName, category: 'RecurrencePatternMonthWeekday.Weekday',                  table: 'recurrence_pattern_month_weekday',       fkColumn: 'rpmwd_weekday_fk' ],
    [ author: authorName, category: 'RecurrencePatternWeek.Weekday',                  table: 'recurrence_pattern_week',                 fkColumn: 'rpw_weekday_fk' ],
    [ author: authorName, category: 'RecurrencePatternYearDate.Month',                    table: 'recurrence_pattern_year_date',             fkColumn: 'rpyd_month_fk' ],
    [ author: authorName, category: 'RecurrencePatternYearMonthWeekday.Weekday',                  table: 'recurrence_pattern_year_month_weekday',  fkColumn: 'rpymwd_weekday_fk' ],
    [ author: authorName, category: 'RecurrencePatternYearMonthWeekday.Month',                    table: 'recurrence_pattern_year_month_weekday',  fkColumn: 'rpymwd_month_fk' ],
    [ author: authorName, category: 'RecurrencePatternYearWeekday.Weekday',                  table: 'recurrence_pattern_year_weekday',        fkColumn: 'rpywd_weekday_fk' ],
    [ author: authorName, category: 'ChronologyTemplateMetadataRule.Format',      table: 'chronology_template_metadata_rule',  fkColumn: 'ctmr_template_metadata_rule_format_fk' ],
    [ author: authorName, category: 'ChronologyMonthTMRF.MonthFormat',              table: 'chronology_monthtmrf', fkColumn: 'cmtmrf_month_format_fk' ],
    [ author: authorName, category: 'ChronologyMonthTMRF.YearFormat',               table: 'chronology_monthtmrf', fkColumn: 'cmtmrf_year_format_fk' ],
    [ author: authorName, category: 'ChronologyYearTMRF.YearFormat',               table: 'chronology_yeartmrf', fkColumn: 'cytmrf_year_format_fk' ],
    [ author: authorName, category: 'EnumerationNumericLevelTMRF.Format',                   table: 'enumeration_numeric_leveltmrf', fkColumn: 'enltmrf_format_fk' ],
    [ author: authorName, category: 'EnumerationNumericLevelTMRF.Sequence',                 table: 'enumeration_numeric_leveltmrf', fkColumn: 'enltmrf_sequence_fk' ]
  ]

  // Generate the changesets to create missing refDataValues
  migrationsToFix.eachWithIndex { config, index ->
    def sequence = String.format('%03d', index + 1)
    def changeSetId = "${idPrefix}-${sequence}"
    def missingValueName = "missing${config.category.split('\\.').last()}RefDataValue"

    changeSet(author: authorName, id: changeSetId) {
      comment("Fixing orphaned data for ${config.table}.${config.fkColumn}")

      // Create the refdata category if it doesn't exist
      grailsChange {
        change {
          sql.execute("""
            INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
            SELECT md5(random()::text || clock_timestamp()::text), 0, '${config.category}', false
            WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = '${config.category}');
          """.toString())
        }
      }

      // Create the "missing" refdata value if it doesn't exist
      grailsChange {
        change {
          sql.execute("""
            INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
            SELECT
              md5(random()::text || clock_timestamp()::text), 0, '${missingValueName}',
              (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = '${config.category}'),
              '${missingValueName}'
            WHERE NOT EXISTS (
              SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = '${config.category}' AND rdv.rdv_value = '${missingValueName}'
            );
          """.toString())
        }
      }

      // Update the target table to fix orphaned foreign keys
      grailsChange {
        change {
          sql.execute("""
            UPDATE ${database.defaultSchemaName}.${config.table}
            SET
              ${config.fkColumn} = (
                SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
                JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
                WHERE rdc.rdc_description = '${config.category}' AND rdv.rdv_value = '${missingValueName}' LIMIT 1
              )
            WHERE NOT EXISTS (
              SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
              WHERE rdv_id = ${database.defaultSchemaName}.${config.table}.${config.fkColumn}
            );
          """.toString())
        }
      }
    }
  }

  // Create the changeset which adds FK constraints to the refDataValue columns in each table.
  changeSet(author: authorName, id: "${idPrefix}-999-add-fk-constraints") {
    migrationsToFix.each { config ->
      // Take everything before _fk and use this as the first part of the constraint name.
      // e.g., 'pkg_lifecycle_status_fk' becomes 'lifecycle_status_to_rdv_fk'
      def coreName = config.fkColumn.replaceAll(/^.*?_/, '').replaceAll(/_fk$/, '')
      def constraintName = "${coreName}_to_rdv_fk"

      // Add the foreign key constraint for each configuration
      addForeignKeyConstraint(
        baseTableName: config.table,
        baseColumnNames: config.fkColumn,
        constraintName: constraintName,
        referencedTableName: "refdata_value",
        referencedColumnNames: "rdv_id",
        deferrable: false,
        initiallyDeferred: false
      )
    }
  }
}