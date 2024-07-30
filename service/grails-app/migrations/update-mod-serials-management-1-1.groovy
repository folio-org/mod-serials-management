databaseChangeLog = {
  changeSet(author: "Jack-Golding (manual)", id: "20240416-1325-001") {
    addColumn(tableName: "chronology_template_metadata_rule") {
      column(name: "ctmr_rule_locale", type: "VARCHAR(36)") { constraints(nullable: "true") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230619-1237-001") {
    createTable(tableName: "template_metadata") {
      column(name: "tm_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "tm_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230619-1237-002") {
    createTable(tableName: "standard_template_metadata") {
      column(name: "stm_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "stm_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "stm_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "stm_date", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "stm_index", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "stm_naive_index", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230619-1237-003") {
    createTable(tableName: "user_configured_template_metadata") {
      column(name: "uctm_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "uctm_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "uctm_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "uctm_user_configured_template_metadata_type_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "uctm_index", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230619-1237-004") {
    createTable(tableName: "user_configured_template_metadata_type") {
      column(name: "uctmt_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "uctmt_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "uctmt_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230619-1237-005") {
    createTable(tableName: "chronologyuctmt") {
      column(name: "uctmt_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cuctmt_weekday", type: "VARCHAR(36)") { constraints(nullable: "true") }
      column(name: "cuctmt_month_day", type: "VARCHAR(36)") { constraints(nullable: "true") }
      column(name: "cuctmt_month", type: "VARCHAR(36)") { constraints(nullable: "true") }
      column(name: "cuctmt_year", type: "VARCHAR(36)") { constraints(nullable: "true") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230619-1237-006") {
    createTable(tableName: "enumerationuctmt") {
      column(name: "uctmt_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "euctmt_value", type: "VARCHAR(36)") { constraints(nullable: "true") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230619-1237-007") {
    createTable(tableName: "enumeration_leveluctmt") {
      column(name: "eluctmt_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "eluctmt_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "eluctmt_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "eluctmt_value", type: "VARCHAR(36)") { constraints(nullable: "true") }
      column(name: "eluctmt_index", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230201-1008-008") {
    addColumn(tableName: "predicted_piece_set") {
      column(name: "first_piece_template_metadata_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "next_piece_template_metadata_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20240725-1425-001") {
    addColumn(tableName: "enumeration_leveluctmt") {
      column(name: "eluctmt_raw_value", type: "INTEGER") { constraints(nullable: "true") }
      column(name: "eluctmt_value_format_fk", type: "VARCHAR(36)") { constraints(nullable: "true") }
    }
  }
}