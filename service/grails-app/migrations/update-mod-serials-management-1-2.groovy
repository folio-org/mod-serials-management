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

  changeSet(author: "Jack-Golding (manual)", id: "20250111-1201-003") {
    dropColumn(columnName: "s_version", tableName: "serial")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250111-1201-004") {
    createTable(tableName: "ruleset_template") {
      column(name: "rt_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "rt_name", type: "TEXT") { constraints(nullable: "false") }
      column(name: "rt_description", type: "TEXT") { constraints(nullable: "true") }
      column(name: "rt_example_label", type: "TEXT") { constraints(nullable: "true") }
      column(name: "rt_ruleset_template_status", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20250111-1201-005") {
    dropForeignKeyConstraint(baseTableName: "serial_ruleset", constraintName: "serial_ruleset_owner_fk")
  }
  // Add migrations for old serial data
  // Add FK constraint to ruleset owner
}