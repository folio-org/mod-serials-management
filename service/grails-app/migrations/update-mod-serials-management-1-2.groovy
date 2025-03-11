databaseChangeLog = {
  changeSet(author: "Jack-Golding (manual)", id: "20241216-1339-001") {
    addColumn(tableName: "predicted_piece_set") {
      column(name: "pps_number_of_cycles", type: "INTEGER") { constraints(nullable: "true") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250111-1201-001") {
    createTable(tableName: "ruleset_owner") {
      column(name: "ro_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "ro_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "ro_date_created", type: "timestamp")
      column(name: "ro_last_updated", type: "timestamp")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250111-1201-002") {
    createTable(tableName: "model_ruleset") {
      column(name: "mr_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "mr_name", type: "TEXT") { constraints(nullable: "false") }
      column(name: "mr_description", type: "TEXT") { constraints(nullable: "true") }
      column(name: "mr_example_label", type: "TEXT") { constraints(nullable: "true") }
      column(name: "mr_ruleset_template_status", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250111-1201-003") {
    // Copy all serial id/version/lastUpdated/dateCreated into rulesetOwner table
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.ruleset_owner(ro_id, ro_version, ro_date_created, ro_last_updated)
          SELECT s_id, s_version, s_date_created, s_last_updated FROM ${database.defaultSchemaName}.serial;
        """.toString())
      }
    }
    
    // Then dropping columns from the original table (except id)
    dropColumn(columnName: "s_version", tableName: "serial")
    dropColumn(columnName: "s_date_created", tableName: "serial")
    dropColumn(columnName: "s_last_updated", tableName: "serial")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250111-1201-004") {
    dropForeignKeyConstraint(baseTableName: "serial_ruleset", constraintName: "serial_ruleset_owner_fk")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250111-1201-005") {
    addUniqueConstraint(columnNames: "ro_id", constraintName: "ro_id_unique", tableName: "ruleset_owner")
    addForeignKeyConstraint(
      baseColumnNames: "sr_owner_fk",
      baseTableName: "serial_ruleset",
      constraintName: "serial_ruleset_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "ro_id",
      referencedTableName: "ruleset_owner"
    )
  }

  // With the removal of TemplateMetadataRule abstract class. both ChronologyTMR and EnumerationTMR will now need to store their own id, owner, version and index
  
  // ENUMERATION CHANGES
  // First we add the new columns to the existing enumeration template metadata rule table
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-001") {
    addColumn(tableName: "enumeration_template_metadata_rule") {
      column(name: "etmr_owner_fk", type: "VARCHAR(36)")
      column(name: "etmr_version", type: "BIGINT")
      column(name: "etmr_index", type:"INTEGER")
    }
  }

  // Rename the id column to etmr_id so that its now differentiated from the chronology template metadata rule class
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-002") {
    renameColumn(tableName: "enumeration_template_metadata_rule", oldColumnName: "tmrt_id", newColumnName: "etmr_id")
  }

  // Now we need to update the 3 columns we added with the data from the template metadata rule type superclasses owner (confusing)
  // Originally the structur was as follows:

  // templateConfig.rules <-one-to-many-> templateMetadataRule <-one-to-one-> templateMetadataType -child-> enumeration/chronologyTemplateMetadataRUle

  // and we wish to chage this to:

  // templateConfig.chronologyRules <-one-to-many-> chronologyTemplateMetadataRule
  // templateConfig.enumerationRules <-one-to-many-> enumerationTemplateMetadataRule

  // In this migration wee therefore need to update the owner, version and index field by grabbing the matching templatemetadataruletype ids owner field
  // and fetch the 3 fields from the templatemetadatarule which is defined as said owner
  changeSet(author: "Jack_golding (manual)", id: "20241205-1222-003") {
		grailsChange {
      change {
          sql.execute("""  
          UPDATE ${database.defaultSchemaName}.enumeration_template_metadata_rule enumeration
		      SET
            etmr_owner_fk = tmr.tmr_owner_fk,
            etmr_version = tmr.tmr_version,
            etmr_index = tmr.tmr_index
		      FROM ${database.defaultSchemaName}.template_metadata_rule tmr
		      LEFT JOIN ${database.defaultSchemaName}.template_metadata_rule_type tmrt
            ON tmrt.tmrt_owner_fk = tmr.tmr_id
		      WHERE enumeration.etmr_id = tmrt.tmrt_id;
          """.toString())
        }
      }
	}

  // CHRONOLOGY CHANGES
  // We then repeat the 3 above steps for the chronology
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-004") {
    addColumn(tableName: "chronology_template_metadata_rule") {
      column(name: "ctmr_owner_fk", type: "VARCHAR(36)")
      column(name: "ctmr_version", type: "BIGINT")
      column(name: "ctmr_index", type:"INTEGER")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-005") {
    renameColumn(tableName: "chronology_template_metadata_rule", oldColumnName: "tmrt_id", newColumnName: "ctmr_id")
  }

  // // This is doing the same as the previous migration but for the chronology template metadata rules
  changeSet(author: "Jack_golding (manual)", id: "20241205-1222-006") {
		grailsChange {
      change {
          sql.execute("""  
          UPDATE ${database.defaultSchemaName}.chronology_template_metadata_rule chronology
		      SET
            ctmr_owner_fk = tmr.tmr_owner_fk,
            ctmr_version = tmr.tmr_version,
            ctmr_index = tmr.tmr_index
		      FROM ${database.defaultSchemaName}.template_metadata_rule tmr
		      LEFT JOIN ${database.defaultSchemaName}.template_metadata_rule_type tmrt
            ON tmrt.tmrt_owner_fk = tmr.tmr_id
		      WHERE chronology.ctmr_id = tmrt.tmrt_id;
          """.toString())
        }
      }
	}

  // // With the removal of TemplateMetadataRuleType abstract class. both ChronologyTMRF and EnumerationTMRF will now need to store their own id, owner, version and index
  
  // // OLD TABLE:
  // // changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-006"){
  // //   createTable(tableName: "template_metadata_rule_format") {
  // //     column(name: "tmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
  // //     column(name: "tmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
  // //     column(name: "tmrf_version", type: "BIGINT") { constraints(nullable: "false") }
  // //   }
  // // }
  
  // ENUMERATION CHANGES
  // Create a new table to track the id, owner and version
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-007") {
    createTable(tableName: "enumeration_template_metadata_rule_format") {
      column(name: "etmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "etmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "etmrf_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  // //Update enumeration TMRF tables "tmrf_id" to match new ETMRF "etmrf_id" as they are now their own distinct classes
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-009") {
    renameColumn(tableName: "enumeration_numerictmrf", oldColumnName: "tmrf_id", newColumnName: "etmrf_id")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-010") {
    renameColumn(tableName: "enumeration_textualtmrf", oldColumnName: "tmrf_id", newColumnName: "etmrf_id")
  }

  // Now we need to insert into the new enumeration table the id, owner and version fields
  // Grabbing them from the old template metadata rule format table but only if the owner id exists as an Id within the enumeration template metadata rule table
  changeSet(author: "Jack_golding (manual)", id: "20241205-1222-015") {
		grailsChange {
      change {
        sql.execute("""  
            INSERT INTO ${database.defaultSchemaName}.enumeration_template_metadata_rule_format (etmrf_id, etmrf_owner_fk, etmrf_version)
            SELECT tmrf_id, tmrf_owner_fk, tmrf_version FROM ${database.defaultSchemaName}.template_metadata_rule_format
            WHERE EXISTS(SELECT FROM ${database.defaultSchemaName}.enumeration_template_metadata_rule WHERE etmr_id = tmrf_owner_fk)
          """.toString())
        }
      }
  }

  // CHRONOLOGY CHANGES
  // These steps are repeated from the enumeration changes above
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-008") {
    createTable(tableName: "chronology_template_metadata_rule_format") {
      column(name: "ctmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "ctmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "ctmrf_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  // //Update chronology TMRF tables "tmrf_id" to match new CTMRF "Ctmrf_id"
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-011") {
    renameColumn(tableName: "chronology_datetmrf", oldColumnName: "tmrf_id", newColumnName: "ctmrf_id")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-012") {
    renameColumn(tableName: "chronology_monthtmrf", oldColumnName: "tmrf_id", newColumnName: "ctmrf_id")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-013") {
    renameColumn(tableName: "chronology_yeartmrf", oldColumnName: "tmrf_id", newColumnName: "ctmrf_id")
  }

  changeSet(author: "Jack_golding (manual)", id: "20241205-1222-014") {
		grailsChange {
      change {
        sql.execute("""  
            INSERT INTO ${database.defaultSchemaName}.chronology_template_metadata_rule_format (ctmrf_id, ctmrf_owner_fk, ctmrf_version)
            SELECT tmrf_id, tmrf_owner_fk, tmrf_version FROM ${database.defaultSchemaName}.template_metadata_rule_format
            WHERE EXISTS(SELECT FROM ${database.defaultSchemaName}.chronology_template_metadata_rule WHERE ctmr_id = tmrf_owner_fk)
          """.toString())
        }
      }
  }
  
  // Then drop tables that are no longer needed
  changeSet(author: "Jack_golding (manual)", id: "20241205-1222-016") {
    dropTable(tableName: "template_metadata_rule")
    dropTable(tableName: "template_metadata_rule_type")
    dropTable(tableName: "template_metadata_rule_format")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250218-1612-001") {
    addColumn(tableName: "enumeration_textual_leveltmrf") {
      column(name: "etltmrf_refdata_value_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250218-1612-002") {
    addColumn(tableName: "enumeration_textualtmrf") {
      column(name: "ettmrf_refdata_category_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250218-1612-003") {
    dropNotNullConstraint(columnName: "etltmrf_value", tableName: "enumeration_textual_leveltmrf")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250311-1044-001") {
    renameColumn(tableName: "enumeration_textual_leveltmrf", oldColumnName: "etltmrf_value", newColumnName: "etltmrf_static_value")
  }
}