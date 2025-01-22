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
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-001") {
    addColumn(tableName: "enumeration_template_metadata_rule") {
      column(name: "etmr_owner_fk", type: "VARCHAR(36)")
      column(name: "etmr_version", type: "BIGINT")
      column(name: "etmr_index", type:"INTEGER")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-002") {
    renameColumn(tableName: "enumeration_template_metadata_rule", oldColumnName: "tmrt_id", newColumnName: "etmr_id")
  }

  changeSet(author: "Jack_golding (manual)", id: "20241205-1222-003") {
		grailsChange {
      change {
        sql.eachRow("SELECT DISTINCT etmr_id FROM ${database.defaultSchemaName}.enumeration_template_metadata_rule".toString()) { def row ->
        
          // Grab owner from template_metadatarule_type super class from matching ID
          sql.rows("""
            SELECT DISTINCT tmrt_id, tmrt_owner_fk FROM ${database.defaultSchemaName}.template_metadata_rule_type WHERE tmrt_id = :etmr_id
          """.toString(), [etmr_id: row.etmr_id]).each {
          
          // Finally update enumeration_template_metadata_rule owner, version index with values from template_metadata_rule
          sql.execute("""  
            UPDATE ${database.defaultSchemaName}.enumeration_template_metadata_rule
            SET (etmr_owner_fk, etmr_version, etmr_index) = ((SELECT tmr_owner_fk FROM ${database.defaultSchemaName}.template_metadata_rule WHERE tmr_id = :owner), (SELECT tmr_version FROM ${database.defaultSchemaName}.template_metadata_rule WHERE tmr_id = :owner), (SELECT tmr_index FROM ${database.defaultSchemaName}.template_metadata_rule WHERE tmr_id = :owner))
            WHERE etmr_id = :etmr_id
          """.toString(), [etmr_id: row.etmr_id, owner: it.tmrt_owner_fk])
          }
        }
      }
    }
	}

  // CHRONOLOGY CHANGES
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

  // This is doing the same as the previous migration but for the chronology template metadata rules
  changeSet(author: "Jack_golding (manual)", id: "20241205-1222-006") {
		grailsChange {
      change {
        sql.eachRow("SELECT DISTINCT ctmr_id FROM ${database.defaultSchemaName}.chronology_template_metadata_rule".toString()) { def row ->
        
          // Grab owner from template_metadatarule_type super class from matching ID
          sql.rows("""
            SELECT DISTINCT tmrt_id, tmrt_owner_fk FROM ${database.defaultSchemaName}.template_metadata_rule_type WHERE tmrt_id = :ctmr_id
          """.toString(), [ctmr_id: row.ctmr_id]).each {
          
          // Finally update chronology_template_metadata_rule owner, version index with values from template_metadata_rule
          sql.execute("""  
            UPDATE ${database.defaultSchemaName}.chronology_template_metadata_rule
            SET (ctmr_owner_fk, ctmr_version, ctmr_index) = ((SELECT tmr_owner_fk FROM ${database.defaultSchemaName}.template_metadata_rule WHERE tmr_id = :owner), (SELECT tmr_version FROM ${database.defaultSchemaName}.template_metadata_rule WHERE tmr_id = :owner), (SELECT tmr_index FROM ${database.defaultSchemaName}.template_metadata_rule WHERE tmr_id = :owner))
            WHERE ctmr_id = :ctmr_id
          """.toString(), [ctmr_id: row.etmr_id, owner: it.tmrt_owner_fk])
          }
        }
      }
    }
	}

  // With the removal of TemplateMetadataRuleType abstract class. both ChronologyTMRF and EnumerationTMRF will now need to store their own id, owner, version and index
  
  // OLD TABLE:
  // changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-006"){
  //   createTable(tableName: "template_metadata_rule_format") {
  //     column(name: "tmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
  //     column(name: "tmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
  //     column(name: "tmrf_version", type: "BIGINT") { constraints(nullable: "false") }
  //   }
  // }
  
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-007") {
    createTable(tableName: "enumeration_template_metadata_rule_format") {
      column(name: "etmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "etmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "etmrf_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-008") {
    createTable(tableName: "chronology_template_metadata_rule_format") {
      column(name: "ctmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "ctmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "ctmrf_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  // //Update enumeration TMRF tables "tmrf_id" to match new ETMRF "etmrf_id"
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-009") {
    renameColumn(tableName: "enumeration_numerictmrf", oldColumnName: "tmrf_id", newColumnName: "etmrf_id")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-010") {
    renameColumn(tableName: "enumeration_textualtmrf", oldColumnName: "tmrf_id", newColumnName: "etmrf_id")
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
            SELECT tmrf_id, tmrf_owner_fk as owner_id, tmrf_version FROM ${database.defaultSchemaName}.template_metadata_rule_format;
            WHERE EXISTS(SELECT FROM ${database.defaultSchemaName}.chronology_template_metadata_rule WHERE ctmr_id = id)
          """.toString(), [ctmr_id: row.etmr_id, owner: it.tmrt_owner_fk])
        }
      }
  }

  //   // Then drop table
  //   dropTable(tableName: "template_metadata_rule_type")
  // }
}