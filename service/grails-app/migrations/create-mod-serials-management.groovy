databaseChangeLog = {

  changeSet(author: "Jack-Golding (manual)", id: "25102022-001") {
    createSequence(sequenceName: "hibernate_sequence")
  }


  changeSet(author: "Jack-Golding (generated)", id: "25102022-002") {

    createTable(tableName: "refdata_category") {
      column(name: "rdc_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "rdc_version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "rdc_description", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "internal", type: "boolean")

    }

    addPrimaryKey(columnNames: "rdc_id", constraintName: "refdata_categoryPK", tableName: "refdata_category")
    addNotNullConstraint (tableName: "refdata_category", columnName: "internal", defaultNullValue: false)

    createTable(tableName: "refdata_value") {
      column(name: "rdv_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "rdv_version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "rdv_value", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "rdv_owner", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "rdv_label", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }


    addPrimaryKey(columnNames: "rdv_id", constraintName: "refdata_valuePK", tableName: "refdata_value")

  }

  changeSet(author: "Jack-Golding (manual)", id: "20230112-1113-001") {
    createTable(tableName: "serial") {
      column(name: "s_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "s_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "s_date_created", type: "timestamp")
      column(name: "s_last_updated", type: "timestamp")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230112-1116-001") {
    createTable(tableName: "serial_order_line") {
      column(name: "rol_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "rol_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "rol_remote_id", type: "VARCHAR(50)") { constraints(nullable: "false") }
      column(name: "sol_owner", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }

    addColumn(tableName: "serial") {
      column(name: "serial_order_line_reference", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230117-1144-001") {
    addColumn(tableName: "serial") {
      column(name: "s_serial_status", type: "VARCHAR(36)")
      column(name: "s_description", type: "TEXT")
    }
  }
}
