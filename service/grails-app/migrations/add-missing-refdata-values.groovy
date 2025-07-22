databaseChangeLog = {
  changeSet(author: "mchaib (manual)", id: "20250723-1100-001") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'ModelRuleset.modelRulesetStatus', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ModelRuleset.modelRulesetStatus');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingmodelRulesetStatusRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ModelRuleset.modelRulesetStatus'),
            'missingmodelRulesetStatusRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'ModelRuleset.modelRulesetStatus' AND rdv.rdv_value = 'missingmodelRulesetStatusRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.model_ruleset
          SET
            mr_ruleset_template_status = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'ModelRuleset.modelRulesetStatus' AND rdv.rdv_value = 'missingmodelRulesetStatusRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.model_ruleset.mr_ruleset_template_status
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "mr_ruleset_template_status", baseTableName: "model_ruleset", constraintName: "ruleset_template_status_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-002") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'Serial.SerialStatus', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Serial.SerialStatus');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingSerialStatusRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Serial.SerialStatus'),
            'missingSerialStatusRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Serial.SerialStatus' AND rdv.rdv_value = 'missingSerialStatusRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.serial
          SET
            s_serial_status = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'Serial.SerialStatus' AND rdv.rdv_value = 'missingSerialStatusRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.serial.s_serial_status
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "s_serial_status", baseTableName: "serial", constraintName: "serial_status_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-003") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'SerialRuleset.RulesetStatus', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'SerialRuleset.RulesetStatus');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingRulesetStatusRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'SerialRuleset.RulesetStatus'),
            'missingRulesetStatusRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'SerialRuleset.RulesetStatus' AND rdv.rdv_value = 'missingRulesetStatusRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.serial_ruleset
          SET
            sr_ruleset_status_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'SerialRuleset.RulesetStatus' AND rdv.rdv_value = 'missingRulesetStatusRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.serial_ruleset.sr_ruleset_status_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "sr_ruleset_status_fk", baseTableName: "serial_ruleset", constraintName: "ruleset_status_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-004") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationRule.TimeUnit', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationRule.TimeUnit');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingTimeUnitRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationRule.TimeUnit'),
            'missingTimeUnitRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationRule.TimeUnit' AND rdv.rdv_value = 'missingTimeUnitRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_rule
          SET
            cr_time_unit_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationRule.TimeUnit' AND rdv.rdv_value = 'missingTimeUnitRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_rule.cr_time_unit_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cr_time_unit_fk", baseTableName: "combination_rule", constraintName: "time_unit_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-005") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationRule.PatternType', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationRule.PatternType');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingPatternTypeRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationRule.PatternType'),
            'missingPatternTypeRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationRule.PatternType' AND rdv.rdv_value = 'missingPatternTypeRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_rule
          SET
            cr_pattern_type_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationRule.PatternType' AND rdv.rdv_value = 'missingPatternTypeRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_rule.cr_pattern_type_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cr_pattern_type_fk", baseTableName: "combination_rule", constraintName: "pattern_type_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-006") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationPatternDayMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternDayMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternDayMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationPatternDayMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_day_month
          SET
            cpdm_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationPatternDayMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_pattern_day_month.cpdm_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpdm_month_fk", baseTableName: "combination_pattern_day_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-007") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationPatternDayWeek.Weekday', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternDayWeek.Weekday');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternDayWeek.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationPatternDayWeek.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_day_week
          SET
            cpdw_weekday_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationPatternDayWeek.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_pattern_day_week.cpdw_weekday_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpdw_weekday_fk", baseTableName: "combination_pattern_day_week", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-008") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationPatternDayWeekMonth.Weekday', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternDayWeekMonth.Weekday');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternDayWeekMonth.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationPatternDayWeekMonth.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_day_week_month
          SET
            cpdwm_weekday_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationPatternDayWeekMonth.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_pattern_day_week_month.cpdwm_weekday_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpdwm_weekday_fk", baseTableName: "combination_pattern_day_week_month", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-009") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationPatternDayWeekMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternDayWeekMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternDayWeekMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationPatternDayWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_day_week_month
          SET
            cpdwm_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationPatternDayWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_pattern_day_week_month.cpdwm_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpdwm_month_fk", baseTableName: "combination_pattern_day_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-010") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationPatternDayWeekday.Weekday', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternDayWeekday.Weekday');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternDayWeekday.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationPatternDayWeekday.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_day_weekday
          SET
            cpdwd_weekday_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationPatternDayWeekday.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_pattern_day_weekday.cpdwd_weekday_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpdwd_weekday_fk", baseTableName: "combination_pattern_day_weekday", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-011") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationPatternIssueMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternIssueMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternIssueMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationPatternIssueMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_issue_month
          SET
            cpim_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationPatternIssueMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_pattern_issue_month.cpim_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpim_month_fk", baseTableName: "combination_pattern_issue_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-012") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationPatternIssueWeekMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternIssueWeekMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternIssueWeekMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationPatternIssueWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_issue_week_month
          SET
            cpiwm_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationPatternIssueWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_pattern_issue_week_month.cpiwm_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpiwm_month_fk", baseTableName: "combination_pattern_issue_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-013") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationPatternMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationPatternMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_month
          SET
            cpm_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationPatternMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_pattern_month.cpm_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpm_month_fk", baseTableName: "combination_pattern_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-014") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'CombinationPatternWeekMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternWeekMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationPatternWeekMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationPatternWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_week_month
          SET
            cpwm_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'CombinationPatternWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.combination_pattern_week_month.cpwm_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpwm_month_fk", baseTableName: "combination_pattern_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-015") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'EnumerationLevelUCTMT.ValueFormat', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'EnumerationLevelUCTMT.ValueFormat');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingValueFormatRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'EnumerationLevelUCTMT.ValueFormat'),
            'missingValueFormatRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'EnumerationLevelUCTMT.ValueFormat' AND rdv.rdv_value = 'missingValueFormatRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.enumeration_leveluctmt
          SET
            eluctmt_value_format_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'EnumerationLevelUCTMT.ValueFormat' AND rdv.rdv_value = 'missingValueFormatRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.enumeration_leveluctmt.eluctmt_value_format_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "eluctmt_value_format_fk", baseTableName: "enumeration_leveluctmt", constraintName: "value_format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-016") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'UserConfiguredTemplateMetadata.userConfiguredTemplateMetadataType', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'UserConfiguredTemplateMetadata.userConfiguredTemplateMetadataType');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missinguserConfiguredTemplateMetadataTypeRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'UserConfiguredTemplateMetadata.userConfiguredTemplateMetadataType'),
            'missinguserConfiguredTemplateMetadataTypeRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'UserConfiguredTemplateMetadata.userConfiguredTemplateMetadataType' AND rdv.rdv_value = 'missinguserConfiguredTemplateMetadataTypeRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.user_configured_template_metadata
          SET
            uctm_user_configured_template_metadata_type_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'UserConfiguredTemplateMetadata.userConfiguredTemplateMetadataType' AND rdv.rdv_value = 'missinguserConfiguredTemplateMetadataTypeRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.user_configured_template_metadata.uctm_user_configured_template_metadata_type_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "uctm_user_configured_template_metadata_type_fk", baseTableName: "user_configured_template_metadata", constraintName: "user_configured_template_metadata_type_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-017") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionRule.TimeUnit', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionRule.TimeUnit');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingTimeUnitRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionRule.TimeUnit'),
            'missingTimeUnitRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionRule.TimeUnit' AND rdv.rdv_value = 'missingTimeUnitRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_rule
          SET
            or_time_unit_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionRule.TimeUnit' AND rdv.rdv_value = 'missingTimeUnitRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_rule.or_time_unit_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "or_time_unit_fk", baseTableName: "omission_rule", constraintName: "time_unit_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-018") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionRule.PatternType', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionRule.PatternType');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingPatternTypeRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionRule.PatternType'),
            'missingPatternTypeRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionRule.PatternType' AND rdv.rdv_value = 'missingPatternTypeRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_rule
          SET
            or_pattern_type_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionRule.PatternType' AND rdv.rdv_value = 'missingPatternTypeRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_rule.or_pattern_type_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "or_pattern_type_fk", baseTableName: "omission_rule", constraintName: "pattern_type_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-019") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionPatternDayMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternDayMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternDayMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionPatternDayMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_day_month
          SET
            opdm_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionPatternDayMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_pattern_day_month.opdm_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opdm_month_fk", baseTableName: "omission_pattern_day_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-020") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionPatternDayWeek.Weekday', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternDayWeek.Weekday');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternDayWeek.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionPatternDayWeek.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_day_week
          SET
            opdw_weekday_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionPatternDayWeek.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_pattern_day_week.opdw_weekday_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opdw_weekday_fk", baseTableName: "omission_pattern_day_week", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-021") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionPatternDayWeekMonth.Weekday', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternDayWeekMonth.Weekday');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternDayWeekMonth.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionPatternDayWeekMonth.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_day_week_month
          SET
            opdwm_weekday_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionPatternDayWeekMonth.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_pattern_day_week_month.opdwm_weekday_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opdwm_weekday_fk", baseTableName: "omission_pattern_day_week_month", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-022") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionPatternDayWeekMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternDayWeekMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternDayWeekMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionPatternDayWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_day_week_month
          SET
            opdwm_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionPatternDayWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_pattern_day_week_month.opdwm_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opdwm_month_fk", baseTableName: "omission_pattern_day_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-023") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionPatternDayWeekday.Weekday', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternDayWeekday.Weekday');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternDayWeekday.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionPatternDayWeekday.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_day_weekday
          SET
            opdwd_weekday_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionPatternDayWeekday.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_pattern_day_weekday.opdwd_weekday_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opdwd_weekday_fk", baseTableName: "omission_pattern_day_weekday", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-024") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionPatternIssueMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternIssueMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternIssueMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionPatternIssueMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_issue_month
          SET
            opim_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionPatternIssueMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_pattern_issue_month.opim_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opim_month_fk", baseTableName: "omission_pattern_issue_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-025") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionPatternIssueWeekMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternIssueWeekMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternIssueWeekMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionPatternIssueWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_issue_week_month
          SET
            opiwm_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionPatternIssueWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_pattern_issue_week_month.opiwm_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opiwm_month_fk", baseTableName: "omission_pattern_issue_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-026") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionPatternMonth.MonthFrom', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternMonth.MonthFrom');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthFromRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternMonth.MonthFrom'),
            'missingMonthFromRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionPatternMonth.MonthFrom' AND rdv.rdv_value = 'missingMonthFromRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_month
          SET
            opm_month_from_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionPatternMonth.MonthFrom' AND rdv.rdv_value = 'missingMonthFromRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_pattern_month.opm_month_from_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opm_month_from_fk", baseTableName: "omission_pattern_month", constraintName: "month_from_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-027") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionPatternMonth.MonthTo', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternMonth.MonthTo');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthToRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternMonth.MonthTo'),
            'missingMonthToRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionPatternMonth.MonthTo' AND rdv.rdv_value = 'missingMonthToRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_month
          SET
            opm_month_to_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionPatternMonth.MonthTo' AND rdv.rdv_value = 'missingMonthToRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_pattern_month.opm_month_to_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opm_month_to_fk", baseTableName: "omission_pattern_month", constraintName: "month_to_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-028") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'OmissionPatternWeekMonth.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternWeekMonth.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionPatternWeekMonth.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionPatternWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_week_month
          SET
            opwm_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'OmissionPatternWeekMonth.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.omission_pattern_week_month.opwm_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opwm_month_fk", baseTableName: "omission_pattern_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-029") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'Recurrence.TimeUnit', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Recurrence.TimeUnit');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingTimeUnitRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Recurrence.TimeUnit'),
            'missingTimeUnitRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Recurrence.TimeUnit' AND rdv.rdv_value = 'missingTimeUnitRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence
          SET
            r_time_unit_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'Recurrence.TimeUnit' AND rdv.rdv_value = 'missingTimeUnitRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.recurrence.r_time_unit_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "r_time_unit_fk", baseTableName: "recurrence", constraintName: "time_unit_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-030") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'RecurrenceRule.PatternType', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrenceRule.PatternType');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingPatternTypeRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrenceRule.PatternType'),
            'missingPatternTypeRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'RecurrenceRule.PatternType' AND rdv.rdv_value = 'missingPatternTypeRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_rule
          SET
            rr_pattern_type_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'RecurrenceRule.PatternType' AND rdv.rdv_value = 'missingPatternTypeRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.recurrence_rule.rr_pattern_type_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rr_pattern_type_fk", baseTableName: "recurrence_rule", constraintName: "pattern_type_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-031") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'RecurrencePatternMonthWeekday.Weekday', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternMonthWeekday.Weekday');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternMonthWeekday.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'RecurrencePatternMonthWeekday.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_month_weekday
          SET
            rpmwd_weekday_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'RecurrencePatternMonthWeekday.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.recurrence_pattern_month_weekday.rpmwd_weekday_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpmwd_weekday_fk", baseTableName: "recurrence_pattern_month_weekday", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-032") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'RecurrencePatternWeek.Weekday', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternWeek.Weekday');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternWeek.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'RecurrencePatternWeek.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_week
          SET
            rpw_weekday_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'RecurrencePatternWeek.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.recurrence_pattern_week.rpw_weekday_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpw_weekday_fk", baseTableName: "recurrence_pattern_week", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-033") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'RecurrencePatternYearDate.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternYearDate.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternYearDate.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'RecurrencePatternYearDate.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_year_date
          SET
            rpyd_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'RecurrencePatternYearDate.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.recurrence_pattern_year_date.rpyd_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpyd_month_fk", baseTableName: "recurrence_pattern_year_date", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-034") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'RecurrencePatternYearMonthWeekday.Weekday', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternYearMonthWeekday.Weekday');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternYearMonthWeekday.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'RecurrencePatternYearMonthWeekday.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_year_month_weekday
          SET
            rpymwd_weekday_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'RecurrencePatternYearMonthWeekday.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.recurrence_pattern_year_month_weekday.rpymwd_weekday_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpymwd_weekday_fk", baseTableName: "recurrence_pattern_year_month_weekday", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-035") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'RecurrencePatternYearMonthWeekday.Month', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternYearMonthWeekday.Month');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternYearMonthWeekday.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'RecurrencePatternYearMonthWeekday.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_year_month_weekday
          SET
            rpymwd_month_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'RecurrencePatternYearMonthWeekday.Month' AND rdv.rdv_value = 'missingMonthRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.recurrence_pattern_year_month_weekday.rpymwd_month_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpymwd_month_fk", baseTableName: "recurrence_pattern_year_month_weekday", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-036") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'RecurrencePatternYearWeekday.Weekday', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternYearWeekday.Weekday');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'RecurrencePatternYearWeekday.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'RecurrencePatternYearWeekday.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_year_weekday
          SET
            rpywd_weekday_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'RecurrencePatternYearWeekday.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.recurrence_pattern_year_weekday.rpywd_weekday_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpywd_weekday_fk", baseTableName: "recurrence_pattern_year_weekday", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-037") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'ChronologyTemplateMetadataRule.Format', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ChronologyTemplateMetadataRule.Format');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingFormatRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ChronologyTemplateMetadataRule.Format'),
            'missingFormatRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'ChronologyTemplateMetadataRule.Format' AND rdv.rdv_value = 'missingFormatRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.chronology_template_metadata_rule
          SET
            ctmr_template_metadata_rule_format_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'ChronologyTemplateMetadataRule.Format' AND rdv.rdv_value = 'missingFormatRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.chronology_template_metadata_rule.ctmr_template_metadata_rule_format_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "ctmr_template_metadata_rule_format_fk", baseTableName: "chronology_template_metadata_rule", constraintName: "template_metadata_rule_format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-038") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'ChronologyMonthTMRF.MonthFormat', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ChronologyMonthTMRF.MonthFormat');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthFormatRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ChronologyMonthTMRF.MonthFormat'),
            'missingMonthFormatRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'ChronologyMonthTMRF.MonthFormat' AND rdv.rdv_value = 'missingMonthFormatRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.chronology_monthtmrf
          SET
            cmtmrf_month_format_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'ChronologyMonthTMRF.MonthFormat' AND rdv.rdv_value = 'missingMonthFormatRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.chronology_monthtmrf.cmtmrf_month_format_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cmtmrf_month_format_fk", baseTableName: "chronology_monthtmrf", constraintName: "month_format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-039") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'ChronologyMonthTMRF.YearFormat', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ChronologyMonthTMRF.YearFormat');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingYearFormatRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ChronologyMonthTMRF.YearFormat'),
            'missingYearFormatRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'ChronologyMonthTMRF.YearFormat' AND rdv.rdv_value = 'missingYearFormatRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.chronology_monthtmrf
          SET
            cmtmrf_year_format_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'ChronologyMonthTMRF.YearFormat' AND rdv.rdv_value = 'missingYearFormatRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.chronology_monthtmrf.cmtmrf_year_format_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cmtmrf_year_format_fk", baseTableName: "chronology_monthtmrf", constraintName: "year_format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-040") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'ChronologyYearTMRF.YearFormat', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ChronologyYearTMRF.YearFormat');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingYearFormatRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ChronologyYearTMRF.YearFormat'),
            'missingYearFormatRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'ChronologyYearTMRF.YearFormat' AND rdv.rdv_value = 'missingYearFormatRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.chronology_yeartmrf
          SET
            cytmrf_year_format_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'ChronologyYearTMRF.YearFormat' AND rdv.rdv_value = 'missingYearFormatRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.chronology_yeartmrf.cytmrf_year_format_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cytmrf_year_format_fk", baseTableName: "chronology_yeartmrf", constraintName: "year_format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-041") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'EnumerationNumericLevelTMRF.Format', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'EnumerationNumericLevelTMRF.Format');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingFormatRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'EnumerationNumericLevelTMRF.Format'),
            'missingFormatRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'EnumerationNumericLevelTMRF.Format' AND rdv.rdv_value = 'missingFormatRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.enumeration_numeric_leveltmrf
          SET
            enltmrf_format_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'EnumerationNumericLevelTMRF.Format' AND rdv.rdv_value = 'missingFormatRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.enumeration_numeric_leveltmrf.enltmrf_format_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "enltmrf_format_fk", baseTableName: "enumeration_numeric_leveltmrf", constraintName: "format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250723-1100-042") {
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
          SELECT md5(random()::text || clock_timestamp()::text), 0, 'EnumerationNumericLevelTMRF.Sequence', false
          WHERE NOT EXISTS (SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'EnumerationNumericLevelTMRF.Sequence');
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingSequenceRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'EnumerationNumericLevelTMRF.Sequence'),
            'missingSequenceRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'EnumerationNumericLevelTMRF.Sequence' AND rdv.rdv_value = 'missingSequenceRefDataValue'
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.enumeration_numeric_leveltmrf
          SET
            enltmrf_sequence_fk = (
              SELECT rdv.rdv_id FROM ${database.defaultSchemaName}.refdata_value rdv
              JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
              WHERE rdc.rdc_description = 'EnumerationNumericLevelTMRF.Sequence' AND rdv.rdv_value = 'missingSequenceRefDataValue' LIMIT 1
            )
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = ${database.defaultSchemaName}.enumeration_numeric_leveltmrf.enltmrf_sequence_fk
          );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "enltmrf_sequence_fk", baseTableName: "enumeration_numeric_leveltmrf", constraintName: "sequence_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

}
