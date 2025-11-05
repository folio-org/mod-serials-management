databaseChangeLog = {


  changeSet(author: "CalamityC (manual)", id: "20251103-1400-001") {
    grailsChange {
      change {
        // Create category 'ModelRuleset.ModelRulesetStatus' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'ModelRuleset.ModelRulesetStatus' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'ModelRuleset.ModelRulesetStatus'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingModelRulesetStatusRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ModelRuleset.ModelRulesetStatus'),
            'missingModelRulesetStatusRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'ModelRuleset.ModelRulesetStatus' AND rdv.rdv_value = 'missingModelRulesetStatusRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.model_ruleset mr
              WHERE mr.mr_ruleset_template_status IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = mr.mr_ruleset_template_status
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.model_ruleset table
          SET mr_ruleset_template_status = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'ModelRuleset.ModelRulesetStatus'
            AND rv.rdv_value = 'missingModelRulesetStatusRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.mr_ruleset_template_status
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "mr_ruleset_template_status", baseTableName: "model_ruleset", constraintName: "ruleset_template_status_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-002") {
    grailsChange {
      change {
        // Create category 'Serial.SerialStatus' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Serial.SerialStatus' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Serial.SerialStatus'
						);
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
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.serial
              WHERE serial.s_serial_status IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = serial.s_serial_status
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.serial table
          SET s_serial_status = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Serial.SerialStatus'
            AND rv.rdv_value = 'missingSerialStatusRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.s_serial_status
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "s_serial_status", baseTableName: "serial", constraintName: "serial_status_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-003") {
    grailsChange {
      change {
        // Create category 'SerialRuleset.RulesetStatus' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'SerialRuleset.RulesetStatus' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'SerialRuleset.RulesetStatus'
						);
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
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.serial_ruleset
              WHERE serial_ruleset.sr_ruleset_status_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = serial_ruleset.sr_ruleset_status_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.serial_ruleset table
          SET sr_ruleset_status_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'SerialRuleset.RulesetStatus'
            AND rv.rdv_value = 'missingRulesetStatusRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.sr_ruleset_status_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "sr_ruleset_status_fk", baseTableName: "serial_ruleset", constraintName: "ruleset_status_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-004") {
    grailsChange {
      change {
        // Create category 'CombinationRule.TimeUnits' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'CombinationRule.TimeUnits' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'CombinationRule.TimeUnits'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingTimeUnitsRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'CombinationRule.TimeUnits'),
            'missingTimeUnitsRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'CombinationRule.TimeUnits' AND rdv.rdv_value = 'missingTimeUnitsRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_rule
              WHERE combination_rule.cr_time_unit_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = combination_rule.cr_time_unit_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_rule table
          SET cr_time_unit_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'CombinationRule.TimeUnits'
            AND rv.rdv_value = 'missingTimeUnitsRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cr_time_unit_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cr_time_unit_fk", baseTableName: "combination_rule", constraintName: "time_unit_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-005") {
    grailsChange {
      change {
        // Create category 'CombinationRule.PatternType' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'CombinationRule.PatternType' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'CombinationRule.PatternType'
						);
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
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_rule
              WHERE combination_rule.cr_pattern_type_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = combination_rule.cr_pattern_type_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_rule table
          SET cr_pattern_type_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'CombinationRule.PatternType'
            AND rv.rdv_value = 'missingPatternTypeRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cr_pattern_type_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cr_pattern_type_fk", baseTableName: "combination_rule", constraintName: "pattern_type_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-006") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_pattern_day_month cpdm
              WHERE cpdm.cpdm_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = cpdm.cpdm_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_day_month table
          SET cpdm_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cpdm_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpdm_month_fk", baseTableName: "combination_pattern_day_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-007") {
    grailsChange {
      change {
        // Create category 'Global.Weekday' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Weekday' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Weekday'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_pattern_day_week cpdw
              WHERE cpdw.cpdw_weekday_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = cpdw.cpdw_weekday_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_day_week table
          SET cpdw_weekday_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Weekday'
            AND rv.rdv_value = 'missingWeekdayRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cpdw_weekday_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpdw_weekday_fk", baseTableName: "combination_pattern_day_week", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-008") {
    grailsChange {
      change {
        // Create category 'Global.Weekday' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Weekday' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Weekday'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_pattern_day_week_month cpdwm
              WHERE cpdwm.cpdwm_weekday_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = cpdwm.cpdwm_weekday_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_day_week_month table
          SET cpdwm_weekday_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Weekday'
            AND rv.rdv_value = 'missingWeekdayRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cpdwm_weekday_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpdwm_weekday_fk", baseTableName: "combination_pattern_day_week_month", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-009") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_pattern_day_week_month cpdwm
              WHERE cpdwm.cpdwm_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = cpdwm.cpdwm_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_day_week_month table
          SET cpdwm_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cpdwm_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpdwm_month_fk", baseTableName: "combination_pattern_day_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-010") {
    grailsChange {
      change {
        // Create category 'Global.Weekday' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Weekday' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Weekday'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_pattern_day_weekday cpdwd
              WHERE cpdwd.cpdwd_weekday_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = cpdwd.cpdwd_weekday_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_day_weekday table
          SET cpdwd_weekday_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Weekday'
            AND rv.rdv_value = 'missingWeekdayRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cpdwd_weekday_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpdwd_weekday_fk", baseTableName: "combination_pattern_day_weekday", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-011") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_pattern_issue_month cpim
              WHERE cpim.cpim_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = cpim.cpim_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_issue_month table
          SET cpim_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cpim_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpim_month_fk", baseTableName: "combination_pattern_issue_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-012") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_pattern_issue_week_month cpiwm
              WHERE cpiwm.cpiwm_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = cpiwm.cpiwm_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_issue_week_month table
          SET cpiwm_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cpiwm_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpiwm_month_fk", baseTableName: "combination_pattern_issue_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-013") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_pattern_month
              WHERE combination_pattern_month.cpm_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = combination_pattern_month.cpm_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_month table
          SET cpm_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cpm_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpm_month_fk", baseTableName: "combination_pattern_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-014") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.combination_pattern_week_month cpwm
              WHERE cpwm.cpwm_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = cpwm.cpwm_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.combination_pattern_week_month table
          SET cpwm_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cpwm_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cpwm_month_fk", baseTableName: "combination_pattern_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-015") {
    grailsChange {
      change {
        // Create category 'EnumerationNumericLevelTMRF.Format' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'EnumerationNumericLevelTMRF.Format' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'EnumerationNumericLevelTMRF.Format'
						);
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
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.enumeration_leveluctmt
              WHERE enumeration_leveluctmt.eluctmt_value_format_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = enumeration_leveluctmt.eluctmt_value_format_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.enumeration_leveluctmt table
          SET eluctmt_value_format_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'EnumerationNumericLevelTMRF.Format'
            AND rv.rdv_value = 'missingFormatRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.eluctmt_value_format_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "eluctmt_value_format_fk", baseTableName: "enumeration_leveluctmt", constraintName: "value_format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-016") {
    grailsChange {
      change {
        // Create category 'UserConfiguredTemplateMetadata.UserConfiguredTemplateMetadataType' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'UserConfiguredTemplateMetadata.UserConfiguredTemplateMetadataType' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'UserConfiguredTemplateMetadata.UserConfiguredTemplateMetadataType'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingUserConfiguredTemplateMetadataTypeRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'UserConfiguredTemplateMetadata.UserConfiguredTemplateMetadataType'),
            'missingUserConfiguredTemplateMetadataTypeRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'UserConfiguredTemplateMetadata.UserConfiguredTemplateMetadataType' AND rdv.rdv_value = 'missingUserConfiguredTemplateMetadataTypeRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.user_configured_template_metadata uctm
              WHERE uctm.uctm_user_configured_template_metadata_type_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = uctm.uctm_user_configured_template_metadata_type_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.user_configured_template_metadata table
          SET uctm_user_configured_template_metadata_type_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'UserConfiguredTemplateMetadata.UserConfiguredTemplateMetadataType'
            AND rv.rdv_value = 'missingUserConfiguredTemplateMetadataTypeRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.uctm_user_configured_template_metadata_type_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "uctm_user_configured_template_metadata_type_fk", baseTableName: "user_configured_template_metadata", constraintName: "user_configured_template_metadata_type_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-017") {
    grailsChange {
      change {
        // Create category 'OmissionRule.TimeUnits' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'OmissionRule.TimeUnits' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'OmissionRule.TimeUnits'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingTimeUnitsRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'OmissionRule.TimeUnits'),
            'missingTimeUnitsRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'OmissionRule.TimeUnits' AND rdv.rdv_value = 'missingTimeUnitsRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_rule
              WHERE omission_rule.or_time_unit_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = omission_rule.or_time_unit_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_rule table
          SET or_time_unit_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'OmissionRule.TimeUnits'
            AND rv.rdv_value = 'missingTimeUnitsRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.or_time_unit_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "or_time_unit_fk", baseTableName: "omission_rule", constraintName: "time_unit_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-018") {
    grailsChange {
      change {
        // Create category 'OmissionRule.PatternType' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'OmissionRule.PatternType' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'OmissionRule.PatternType'
						);
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
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_rule
              WHERE omission_rule.or_pattern_type_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = omission_rule.or_pattern_type_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_rule table
          SET or_pattern_type_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'OmissionRule.PatternType'
            AND rv.rdv_value = 'missingPatternTypeRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.or_pattern_type_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "or_pattern_type_fk", baseTableName: "omission_rule", constraintName: "pattern_type_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-019") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_pattern_day_month opdm
              WHERE opdm.opdm_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = opdm.opdm_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_day_month table
          SET opdm_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.opdm_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opdm_month_fk", baseTableName: "omission_pattern_day_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-020") {
    grailsChange {
      change {
        // Create category 'Global.Weekday' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Weekday' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Weekday'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_pattern_day_week opdw
              WHERE opdw.opdw_weekday_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = opdw.opdw_weekday_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_day_week table
          SET opdw_weekday_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Weekday'
            AND rv.rdv_value = 'missingWeekdayRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.opdw_weekday_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opdw_weekday_fk", baseTableName: "omission_pattern_day_week", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-021") {
    grailsChange {
      change {
        // Create category 'Global.Weekday' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Weekday' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Weekday'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_pattern_day_week_month opdwm
              WHERE opdwm.opdwm_weekday_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = opdwm.opdwm_weekday_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_day_week_month table
          SET opdwm_weekday_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Weekday'
            AND rv.rdv_value = 'missingWeekdayRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.opdwm_weekday_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opdwm_weekday_fk", baseTableName: "omission_pattern_day_week_month", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-022") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_pattern_day_week_month opdwm
              WHERE opdwm.opdwm_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = opdwm.opdwm_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_day_week_month table
          SET opdwm_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.opdwm_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opdwm_month_fk", baseTableName: "omission_pattern_day_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-023") {
    grailsChange {
      change {
        // Create category 'Global.Weekday' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Weekday' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Weekday'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_pattern_day_weekday opdwd
              WHERE opdwd.opdwd_weekday_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = opdwd.opdwd_weekday_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_day_weekday table
          SET opdwd_weekday_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Weekday'
            AND rv.rdv_value = 'missingWeekdayRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.opdwd_weekday_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opdwd_weekday_fk", baseTableName: "omission_pattern_day_weekday", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-024") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_pattern_issue_month opim
              WHERE opim.opim_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = opim.opim_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_issue_month table
          SET opim_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.opim_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opim_month_fk", baseTableName: "omission_pattern_issue_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-025") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_pattern_issue_week_month opiwm
              WHERE opiwm.opiwm_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = opiwm.opiwm_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_issue_week_month table
          SET opiwm_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.opiwm_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opiwm_month_fk", baseTableName: "omission_pattern_issue_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-026") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_pattern_month opm
              WHERE opm.opm_month_from_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = opm.opm_month_from_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_month table
          SET opm_month_from_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.opm_month_from_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opm_month_from_fk", baseTableName: "omission_pattern_month", constraintName: "month_from_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-027") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_pattern_month opm
              WHERE opm.opm_month_to_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = opm.opm_month_to_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_month table
          SET opm_month_to_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.opm_month_to_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opm_month_to_fk", baseTableName: "omission_pattern_month", constraintName: "month_to_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-028") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.omission_pattern_week_month opwm
              WHERE opwm.opwm_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = opwm.opwm_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.omission_pattern_week_month table
          SET opwm_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.opwm_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "opwm_month_fk", baseTableName: "omission_pattern_week_month", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-029") {
    grailsChange {
      change {
        // Create category 'Recurrence.TimeUnits' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Recurrence.TimeUnits' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Recurrence.TimeUnits'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingTimeUnitsRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Recurrence.TimeUnits'),
            'missingTimeUnitsRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Recurrence.TimeUnits' AND rdv.rdv_value = 'missingTimeUnitsRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.recurrence
              WHERE recurrence.r_time_unit_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = recurrence.r_time_unit_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence table
          SET r_time_unit_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Recurrence.TimeUnits'
            AND rv.rdv_value = 'missingTimeUnitsRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.r_time_unit_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "r_time_unit_fk", baseTableName: "recurrence", constraintName: "time_unit_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-030") {
    grailsChange {
      change {
        // Create category 'RecurrenceRule.PatternType' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'RecurrenceRule.PatternType' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'RecurrenceRule.PatternType'
						);
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
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.recurrence_rule
              WHERE recurrence_rule.rr_pattern_type_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = recurrence_rule.rr_pattern_type_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_rule table
          SET rr_pattern_type_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'RecurrenceRule.PatternType'
            AND rv.rdv_value = 'missingPatternTypeRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.rr_pattern_type_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rr_pattern_type_fk", baseTableName: "recurrence_rule", constraintName: "pattern_type_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-031") {
    grailsChange {
      change {
        // Create category 'Global.Weekday' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Weekday' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Weekday'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.recurrence_pattern_month_weekday rpmwd
              WHERE rpmwd.rpmwd_weekday_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = rpmwd.rpmwd_weekday_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_month_weekday table
          SET rpmwd_weekday_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Weekday'
            AND rv.rdv_value = 'missingWeekdayRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.rpmwd_weekday_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpmwd_weekday_fk", baseTableName: "recurrence_pattern_month_weekday", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-032") {
    grailsChange {
      change {
        // Create category 'Global.Weekday' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Weekday' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Weekday'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.recurrence_pattern_week rpw
              WHERE rpw.rpw_weekday_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = rpw.rpw_weekday_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_week table
          SET rpw_weekday_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Weekday'
            AND rv.rdv_value = 'missingWeekdayRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.rpw_weekday_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpw_weekday_fk", baseTableName: "recurrence_pattern_week", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-033") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.recurrence_pattern_year_date rpyd
              WHERE rpyd.rpyd_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = rpyd.rpyd_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_year_date table
          SET rpyd_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.rpyd_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpyd_month_fk", baseTableName: "recurrence_pattern_year_date", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-034") {
    grailsChange {
      change {
        // Create category 'Global.Weekday' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Weekday' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Weekday'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.recurrence_pattern_year_month_weekday rpymwd
              WHERE rpymwd.rpymwd_weekday_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = rpymwd.rpymwd_weekday_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_year_month_weekday table
          SET rpymwd_weekday_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Weekday'
            AND rv.rdv_value = 'missingWeekdayRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.rpymwd_weekday_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpymwd_weekday_fk", baseTableName: "recurrence_pattern_year_month_weekday", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-035") {
    grailsChange {
      change {
        // Create category 'Global.Month' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Month' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Month'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Month'),
            'missingMonthRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Month' AND rdv.rdv_value = 'missingMonthRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.recurrence_pattern_year_month_weekday rpymwd
              WHERE rpymwd.rpymwd_month_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = rpymwd.rpymwd_month_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_year_month_weekday table
          SET rpymwd_month_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Month'
            AND rv.rdv_value = 'missingMonthRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.rpymwd_month_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpymwd_month_fk", baseTableName: "recurrence_pattern_year_month_weekday", constraintName: "month_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-036") {
    grailsChange {
      change {
        // Create category 'Global.Weekday' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.Weekday' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.Weekday'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingWeekdayRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.Weekday'),
            'missingWeekdayRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.Weekday' AND rdv.rdv_value = 'missingWeekdayRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.recurrence_pattern_year_weekday rpywd
              WHERE rpywd.rpywd_weekday_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = rpywd.rpywd_weekday_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.recurrence_pattern_year_weekday table
          SET rpywd_weekday_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.Weekday'
            AND rv.rdv_value = 'missingWeekdayRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.rpywd_weekday_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "rpywd_weekday_fk", baseTableName: "recurrence_pattern_year_weekday", constraintName: "weekday_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-037") {
    grailsChange {
      change {
        // Create category 'ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingTemplateMetadataRuleFormatRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat'),
            'missingTemplateMetadataRuleFormatRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat' AND rdv.rdv_value = 'missingTemplateMetadataRuleFormatRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.chronology_template_metadata_rule ctmr
              WHERE ctmr.ctmr_template_metadata_rule_format_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = ctmr.ctmr_template_metadata_rule_format_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.chronology_template_metadata_rule table
          SET ctmr_template_metadata_rule_format_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'ChronologyTemplateMetadataRule.TemplateMetadataRuleFormat'
            AND rv.rdv_value = 'missingTemplateMetadataRuleFormatRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.ctmr_template_metadata_rule_format_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "ctmr_template_metadata_rule_format_fk", baseTableName: "chronology_template_metadata_rule", constraintName: "template_metadata_rule_format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-038") {
    grailsChange {
      change {
        // Create category 'Global.MonthFormat' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.MonthFormat' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.MonthFormat'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingMonthFormatRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.MonthFormat'),
            'missingMonthFormatRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.MonthFormat' AND rdv.rdv_value = 'missingMonthFormatRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.chronology_monthtmrf
              WHERE chronology_monthtmrf.cmtmrf_month_format_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = chronology_monthtmrf.cmtmrf_month_format_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.chronology_monthtmrf table
          SET cmtmrf_month_format_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.MonthFormat'
            AND rv.rdv_value = 'missingMonthFormatRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cmtmrf_month_format_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cmtmrf_month_format_fk", baseTableName: "chronology_monthtmrf", constraintName: "month_format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-039") {
    grailsChange {
      change {
        // Create category 'Global.YearFormat' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.YearFormat' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.YearFormat'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingYearFormatRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.YearFormat'),
            'missingYearFormatRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.YearFormat' AND rdv.rdv_value = 'missingYearFormatRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.chronology_monthtmrf
              WHERE chronology_monthtmrf.cmtmrf_year_format_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = chronology_monthtmrf.cmtmrf_year_format_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.chronology_monthtmrf table
          SET cmtmrf_year_format_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.YearFormat'
            AND rv.rdv_value = 'missingYearFormatRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cmtmrf_year_format_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cmtmrf_year_format_fk", baseTableName: "chronology_monthtmrf", constraintName: "year_format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-040") {
    grailsChange {
      change {
        // Create category 'Global.YearFormat' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'Global.YearFormat' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'Global.YearFormat'
						);
				""".toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT
            md5(random()::text || clock_timestamp()::text), 0, 'missingYearFormatRefDataValue',
            (SELECT rdc_id FROM ${database.defaultSchemaName}.refdata_category WHERE rdc_description = 'Global.YearFormat'),
            'missingYearFormatRefDataValue'
          WHERE NOT EXISTS (
            SELECT 1 FROM ${database.defaultSchemaName}.refdata_value rdv
            JOIN ${database.defaultSchemaName}.refdata_category rdc ON rdv.rdv_owner = rdc.rdc_id
            WHERE rdc.rdc_description = 'Global.YearFormat' AND rdv.rdv_value = 'missingYearFormatRefDataValue'
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.chronology_yeartmrf
              WHERE chronology_yeartmrf.cytmrf_year_format_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = chronology_yeartmrf.cytmrf_year_format_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.chronology_yeartmrf table
          SET cytmrf_year_format_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Global.YearFormat'
            AND rv.rdv_value = 'missingYearFormatRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cytmrf_year_format_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "cytmrf_year_format_fk", baseTableName: "chronology_yeartmrf", constraintName: "year_format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-041") {
    grailsChange {
      change {
        // Create category 'EnumerationNumericLevelTMRF.Format' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'EnumerationNumericLevelTMRF.Format' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'EnumerationNumericLevelTMRF.Format'
						);
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
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.enumeration_numeric_leveltmrf
              WHERE enumeration_numeric_leveltmrf.enltmrf_format_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = enumeration_numeric_leveltmrf.enltmrf_format_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.enumeration_numeric_leveltmrf table
          SET enltmrf_format_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'EnumerationNumericLevelTMRF.Format'
            AND rv.rdv_value = 'missingFormatRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.enltmrf_format_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "enltmrf_format_fk", baseTableName: "enumeration_numeric_leveltmrf", constraintName: "format_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "CalamityC (manual)", id: "20251103-1400-042") {
    grailsChange {
      change {
        // Create category 'EnumerationNumericLevelTMRF.Sequence' if not exists
				sql.execute("""
					INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal)
						SELECT md5(random()::text || clock_timestamp()::text) as id,
						0 as version,
						'EnumerationNumericLevelTMRF.Sequence' as description,
						false as internal
					WHERE
						-- don't recreate category if it already exists
						NOT EXISTS (
						SELECT 1 FROM ${database.defaultSchemaName}.refdata_category WHERE
							rdc_description = 'EnumerationNumericLevelTMRF.Sequence'
						);
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
          )
          AND
          -- create only if there are orphaned FKs
          EXISTS (
            SELECT 1
              FROM ${database.defaultSchemaName}.enumeration_numeric_leveltmrf
              WHERE enumeration_numeric_leveltmrf.enltmrf_sequence_fk IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM ${database.defaultSchemaName}.refdata_value rdv2
                    WHERE rdv2.rdv_id = enumeration_numeric_leveltmrf.enltmrf_sequence_fk
                  )
          );
        """.toString())
      }
    }
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.enumeration_numeric_leveltmrf table
          SET enltmrf_sequence_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'EnumerationNumericLevelTMRF.Sequence'
            AND rv.rdv_value = 'missingSequenceRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.enltmrf_sequence_fk
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "enltmrf_sequence_fk", baseTableName: "enumeration_numeric_leveltmrf", constraintName: "sequence_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

}
