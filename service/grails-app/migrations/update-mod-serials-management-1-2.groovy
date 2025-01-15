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
    dropColumn(columnName: "s_version", tableName: "work")
    dropColumn(columnName: "s_date_created", tableName: "work")
    dropColumn(columnName: "s_last_updated", tableName: "work")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250111-1201-004") {
    dropForeignKeyConstraint(baseTableName: "serial_ruleset", constraintName: "serial_ruleset_owner_fk")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250111-1201-005") {
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
}