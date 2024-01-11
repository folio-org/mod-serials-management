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
  changeSet(author: "Jack-Golding (manual)", id: "20230118-1443-001") {
    createTable(tableName: "serial_note") {
      column(name: "sn_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "sn_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "sn_owner_fk", type:"VARCHAR(36)")
      column(name: "sn_note", type: "TEXT")
    }
  }
  changeSet(author: "Jack-Golding (manual)", id: "20230118-1551-001"){
    addUniqueConstraint(columnNames: "s_id", constraintName: "s_id_unique", tableName: "serial")

    addForeignKeyConstraint(baseColumnNames: "sn_owner_fk",
      baseTableName: "serial_note",
      constraintName: "serial_note_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "s_id",
      referencedTableName: "serial"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230309-1026-001") {
    createTable(tableName: 'serial_ruleset') {
      column(name: "sr_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "sr_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "sr_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "sr_date_created", type: "timestamp")
      column(name: "sr_last_updated", type: "timestamp")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230309-1026-002") {
    addUniqueConstraint(columnNames: "sr_id", constraintName: "sr_id_unique", tableName: "serial_ruleset")
    addForeignKeyConstraint(
      baseColumnNames: "sr_owner_fk",
      baseTableName: "serial_ruleset",
      constraintName: "serial_ruleset_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "s_id",
      referencedTableName: "serial"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230203-1130-001") {
    createTable(tableName: 'recurrence') {
      column(name: "r_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "r_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "r_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "r_time_unit_fk", type: "VARCHAR(36)")
      column(name: "r_issues", type: "BIGINT")
      column(name: "r_period", type: "BIGINT")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230203-1147-001") {
    addForeignKeyConstraint(
      baseColumnNames: "r_owner_fk",
      baseTableName: "recurrence",
      constraintName: "recurrence_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "sr_id",
      referencedTableName: "serial_ruleset"
    )
  }
  changeSet(author: "Jack-Golding (manual)", id: "20230203-1155-001") {
    addForeignKeyConstraint(
      baseColumnNames: "r_time_unit_fk",
      baseTableName: "recurrence",
      constraintName: "recurrence_time_unit_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1040-001") {
    createTable(tableName: "recurrence_rule") {
      column(name: "rr_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "rr_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "rr_ordinal", type: "BIGINT")
      column(name: "rr_pattern_type_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1043-001") {
    addUniqueConstraint(columnNames: "r_id", constraintName: "r_id_unique", tableName: "recurrence")
    addForeignKeyConstraint(
      baseColumnNames: "rr_owner_fk",
      baseTableName: "recurrence_rule",
      constraintName: "recurrence_rule_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "r_id",
      referencedTableName: "recurrence"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1043-002") {
    addForeignKeyConstraint(
      baseColumnNames: "rr_pattern_type_fk",
      baseTableName: "recurrence_rule",
      constraintName: "recurrence_rule_pattern_type_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1151-001") {
    createTable(tableName: "recurrence_pattern") {
      column(name: "rp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "rp_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1151-002") {
    addUniqueConstraint(columnNames: "rr_id", constraintName: "rr_id_unique", tableName: "recurrence_rule")
    addForeignKeyConstraint(
      baseColumnNames: "rp_owner_fk",
      baseTableName: "recurrence_pattern",
      constraintName: "recurrence_pattern_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rr_id",
      referencedTableName: "recurrence_rule"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1223-001") {
    createTable(tableName: "recurrence_pattern_month_date") {
      column(name: "rpmd_day", type: "BIGINT")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1223-002") {
    createTable(tableName: "recurrence_pattern_month_weekday") {
      column(name: "rpmwd_week", type: "BIGINT")
      column(name: "rpmwd_weekday_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1223-003") {
    createTable(tableName: "recurrence_pattern_week") {
      column(name: "rpw_weekday_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1223-004") {
    createTable(tableName: "recurrence_pattern_year_date") {
      column(name: "rpyd_day", type: "BIGINT")
      column(name: "rpyd_month_fk", type: "VARCHAR(36)")    
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1223-005") {
    createTable(tableName: "recurrence_pattern_year_month_weekday") {
      column(name: "rpymwd_week", type: "BIGINT")
      column(name: "rpymwd_weekday_fk", type: "VARCHAR(36)")
      column(name: "rpymwd_month_fk", type: "VARCHAR(36)")    

    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230206-1223-006") {
    createTable(tableName: "recurrence_pattern_year_weekday") {
      column(name: "rpywd_week", type: "BIGINT")
      column(name: "rpywd_weekday_fk", type: "VARCHAR(36)")

    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230207-1134-001") {
    addColumn(tableName: "recurrence_rule") {
      column(name: "rr_version", type: "BIGINT")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230207-1134-002") {
    addColumn(tableName: "recurrence_pattern") {
      column(name: "rp_version", type: "BIGINT")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230209-1407-001") {
    addColumn(tableName: "recurrence_pattern_month_date") {
      column(name: "rp_id", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230209-1407-002") {
    addColumn(tableName: "recurrence_pattern_month_weekday") {
      column(name: "rp_id", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230209-1407-003") {
    addColumn(tableName: "recurrence_pattern_week") {
      column(name: "rp_id", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230209-1407-004") {
    addColumn(tableName: "recurrence_pattern_year_date") {
      column(name: "rp_id", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230209-1407-005") {
    addColumn(tableName: "recurrence_pattern_year_month_weekday") {
      column(name: "rp_id", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230209-1407-006") {
    addColumn(tableName: "recurrence_pattern_year_weekday") {
      column(name: "rp_id", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230209-1407-007") {
    createTable(tableName: "recurrence_pattern_day") {
      column(name: "rp_id", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230213-1106-001") {
    addForeignKeyConstraint(
      baseColumnNames: "rpmwd_weekday_fk",
      baseTableName: "recurrence_pattern_month_weekday",
      constraintName: "recurrence_pattern_month_weekday_weekday_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230213-1106-002") {
    addForeignKeyConstraint(
      baseColumnNames: "rpw_weekday_fk",
      baseTableName: "recurrence_pattern_week",
      constraintName: "recurrence_pattern_week_weekday_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230213-1106-003") {
    addForeignKeyConstraint(
      baseColumnNames: "rpyd_month_fk",
      baseTableName: "recurrence_pattern_year_date",
      constraintName: "recurrence_pattern_year_date_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230213-1106-004") {
    addForeignKeyConstraint(
      baseColumnNames: "rpymwd_weekday_fk",
      baseTableName: "recurrence_pattern_year_month_weekday",
      constraintName: "recurrence_pattern_year_month_weekday_weekday_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230213-1106-005") {
    addForeignKeyConstraint(
      baseColumnNames: "rpymwd_month_fk",
      baseTableName: "recurrence_pattern_year_month_weekday",
      constraintName: "recurrence_pattern_year_month_weekday_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230213-1106-006") {
    addForeignKeyConstraint(
      baseColumnNames: "rpywd_weekday_fk",
      baseTableName: "recurrence_pattern_year_weekday",
      constraintName: "recurrence_pattern_year_weekday_weekday_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-2") {
    modifyDataType( 
      tableName: "recurrence", 
      columnName: "r_issues", 
      newDataType: "INT", 
      confirm: "Successfully updated the r_issues column."
    )
    addNotNullConstraint(columnDataType: "INT", columnName: "r_issues", tableName: "recurrence", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-3") {
    modifyDataType( 
      tableName: "recurrence", 
      columnName: "r_period", 
      newDataType: "INT", 
      confirm: "Successfully updated the r_period column."
    ) 
    addNotNullConstraint(columnDataType: "INT", columnName: "r_period", tableName: "recurrence", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-4") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "r_time_unit_fk", tableName: "recurrence", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-5") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rp_id", tableName: "recurrence_pattern_day", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-6") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rp_id", tableName: "recurrence_pattern_month_date", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-7") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rp_id", tableName: "recurrence_pattern_month_weekday", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-8") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rp_id", tableName: "recurrence_pattern_week", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-9") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rp_id", tableName: "recurrence_pattern_year_date", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-10") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rp_id", tableName: "recurrence_pattern_year_month_weekday", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-11") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rp_id", tableName: "recurrence_pattern_year_weekday", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-12") {
    addNotNullConstraint(columnDataType: "BIGINT", columnName: "rp_version", tableName: "recurrence_pattern", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-13") {
    modifyDataType( 
      tableName: "recurrence_pattern_month_date", 
      columnName: "rpmd_day", 
      newDataType: "INT", 
      confirm: "Successfully updated the rpmd_day column."
    )
    addNotNullConstraint(columnDataType: "INT", columnName: "rpmd_day", tableName: "recurrence_pattern_month_date", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-14") {
    modifyDataType( 
      tableName: "recurrence_pattern_month_weekday", 
      columnName: "rpmwd_week", 
      newDataType: "INT", 
      confirm: "Successfully updated the rpmwd_week column."
    )
    addNotNullConstraint(columnDataType: "INT", columnName: "rpmwd_week", tableName: "recurrence_pattern_month_weekday", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-15") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rpmwd_weekday_fk", tableName: "recurrence_pattern_month_weekday", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-16") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rpw_weekday_fk", tableName: "recurrence_pattern_week", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-17") {
    modifyDataType( 
      tableName: "recurrence_pattern_year_date", 
      columnName: "rpyd_day", 
      newDataType: "INT", 
      confirm: "Successfully updated the rpyd_day column."
    )
    addNotNullConstraint(columnDataType: "INT", columnName: "rpyd_day", tableName: "recurrence_pattern_year_date", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-18") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rpyd_month_fk", tableName: "recurrence_pattern_year_date", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-19") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rpymwd_month_fk", tableName: "recurrence_pattern_year_month_weekday", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-20") {
    modifyDataType( 
      tableName: "recurrence_pattern_year_month_weekday", 
      columnName: "rpymwd_week", 
      newDataType: "INT", 
      confirm: "Successfully updated the rpymwd_week column."
    )
    addNotNullConstraint(columnDataType: "INT", columnName: "rpymwd_week", tableName: "recurrence_pattern_year_month_weekday", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-21") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rpymwd_weekday_fk", tableName: "recurrence_pattern_year_month_weekday", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-22") {
    modifyDataType( 
      tableName: "recurrence_pattern_year_weekday", 
      columnName: "rpywd_week", 
      newDataType: "INT", 
      confirm: "Successfully updated the rpywd_week column."
    )
    addNotNullConstraint(columnDataType: "INT", columnName: "rpywd_week", tableName: "recurrence_pattern_year_weekday", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-23") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rpywd_weekday_fk", tableName: "recurrence_pattern_year_weekday", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-24") {
    modifyDataType( 
      tableName: "recurrence_rule", 
      columnName: "rr_ordinal", 
      newDataType: "INT", 
      confirm: "Successfully updated the rr_ordinal column."
    )
    addNotNullConstraint(columnDataType: "INT", columnName: "rr_ordinal", tableName: "recurrence_rule", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-25") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "rr_pattern_type_fk", tableName: "recurrence_rule", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-26") {
    addNotNullConstraint(columnDataType: "BIGINT", columnName: "rr_version", tableName: "recurrence_rule", validate: "true")
  }

  changeSet(author: "jack-golding (generated)", id: "1679657889431-27") {
    addNotNullConstraint(columnDataType: "VARCHAR(36)", columnName: "sn_owner_fk", tableName: "serial_note", validate: "true")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230328-1518-001") {
    addColumn(tableName: "serial_ruleset") {
      column(name: "sr_ruleset_status_fk", type: "VARCHAR(36)")
      column(name: "sr_description", type: "TEXT")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230328-1518-002") {
    addForeignKeyConstraint(
      baseColumnNames: "sr_ruleset_status_fk",
      baseTableName: "serial_ruleset",
      constraintName: "serial_ruleset_status_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-001") {
    createTable(tableName: "omission") {
      column(name: "o_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "o_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "o_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-002") {
    addForeignKeyConstraint(
      baseColumnNames: "o_owner_fk",
      baseTableName: "omission",
      constraintName: "omission_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "sr_id",
      referencedTableName: "serial_ruleset"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-003") {
    createTable(tableName: "omission_rule") {
      column(name: "or_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "or_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "or_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "or_time_unit_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "or_pattern_type_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-004") {
    addUniqueConstraint(columnNames: "o_id", constraintName: "o_id_unique", tableName: "omission")
    addForeignKeyConstraint(
      baseColumnNames: "or_owner_fk",
      baseTableName: "omission_rule",
      constraintName: "omission_rule_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "o_id",
      referencedTableName: "omission"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-005") {
    addForeignKeyConstraint(
      baseColumnNames: "or_time_unit_fk",
      baseTableName: "omission_rule",
      constraintName: "omission_rule_time_unit_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-006") {
    addForeignKeyConstraint(
      baseColumnNames: "or_pattern_type_fk",
      baseTableName: "omission_rule",
      constraintName: "omission_rule_pattern_type_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-007") {
    createTable(tableName: "omission_pattern") {
      column(name: "op_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "op_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "op_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-008") {
    addUniqueConstraint(columnNames: "or_id", constraintName: "or_id_unique", tableName: "omission_rule")
    addForeignKeyConstraint(
      baseColumnNames: "op_owner_fk",
      baseTableName: "omission_pattern",
      constraintName: "omission_pattern_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "or_id",
      referencedTableName: "omission_rule"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230404-1136-001") {
    createTable(tableName: "omission_pattern_month") {
      column(name: "op_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opm_month_from_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opm_month_to_fk", type: "VARCHAR(36)") 
      column(name: "opm_is_range", type: "BOOLEAN") { constraints(nullable: "false") }        
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230404-1136-002") {
    addForeignKeyConstraint(
      baseColumnNames: "opm_month_to_fk",
      baseTableName: "omission_pattern_month",
      constraintName: "omission_pattern_month_month_to_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230404-1136-003") {
    addForeignKeyConstraint(
      baseColumnNames: "opm_month_from_fk",
      baseTableName: "omission_pattern_month",
      constraintName: "omission_pattern_month_month_from_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230404-1136-004") {
    createTable(tableName: "omission_pattern_week") {
      column(name: "op_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opw_week_from", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opw_week_to", type: "VARCHAR(36)") 
      column(name: "opw_is_range", type: "BOOLEAN") { constraints(nullable: "false") }        
    }
  }

  changeSet(author: "jack-golding (generated)", id: "1682434856888-29") {
    createTable(tableName: "omission_pattern_day") {
      column(name: "op_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opd_day", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "jack-golding (generated)", id: "1682434856888-30") {
    createTable(tableName: "omission_pattern_day_month") {
      column(name: "op_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opdm_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opdm_day", type: "INTEGER") { constraints(nullable: "false") }
    }
  }
  
  changeSet(author: "Jack-Golding (manual)", id: "20230425-1623-001") {
    addForeignKeyConstraint(
      baseColumnNames: "opdm_month_fk",
      baseTableName: "omission_pattern_day_month",
      constraintName: "omission_pattern_day_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1682434856888-31") {
    createTable(tableName: "omission_pattern_day_week") {
      column(name: "op_id", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "opdw_week", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "opdw_weekday_fk", type: "VARCHAR(36)") { constraints(nullable: "false") } 
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230425-1623-002") {
    addForeignKeyConstraint(
      baseColumnNames: "opdw_weekday_fk",
      baseTableName: "omission_pattern_day_week",
      constraintName: "omission_pattern_day_week_weekday_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1682434856888-32") {
    createTable(tableName: "omission_pattern_day_week_month") {
      column(name: "op_id", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "opdwm_week", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "opdwm_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opdwm_weekday_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230425-1623-003") {
    addForeignKeyConstraint(
      baseColumnNames: "opdwm_weekday_fk",
      baseTableName: "omission_pattern_day_week_month",
      constraintName: "omission_pattern_day_week_month_weekday_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230425-1623-004") {
    addForeignKeyConstraint(
      baseColumnNames: "opdwm_month_fk",
      baseTableName: "omission_pattern_day_week_month",
      constraintName: "omission_pattern_day_week_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1682434856888-33") {
    createTable(tableName: "omission_pattern_day_weekday") {
      column(name: "op_id", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "opdwd_weekday_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230425-1623-005") {
    addForeignKeyConstraint(
      baseColumnNames: "opdwd_weekday_fk",
      baseTableName: "omission_pattern_day_weekday",
      constraintName: "omission_pattern_day_weekday_weekday_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1682434856888-34") {
    createTable(tableName: "omission_pattern_issue") {
      column(name: "op_id", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "opi_issue", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "jack-golding (generated)", id: "1682434856888-35") {
    createTable(tableName: "omission_pattern_issue_month") {
      column(name: "op_id", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "opim_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opim_issue", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230425-1623-006") {
    addForeignKeyConstraint(
      baseColumnNames: "opim_month_fk",
      baseTableName: "omission_pattern_issue_month",
      constraintName: "omission_pattern_issue_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1682434856888-36") {
    createTable(tableName: "omission_pattern_issue_week") {
      column(name: "op_id", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "opiw_week", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "opiw_issue", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "jack-golding (generated)", id: "1682434856888-37") {
    createTable(tableName: "omission_pattern_issue_week_month") {
      column(name: "op_id", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "opiwm_week", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "opiwm_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opiwm_issue", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230425-1623-007") {
    addForeignKeyConstraint(
      baseColumnNames: "opiwm_month_fk",
      baseTableName: "omission_pattern_issue_week_month",
      constraintName: "omission_pattern_issue_week_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1682434856888-38") {
    createTable(tableName: "omission_pattern_week_month") {
      column(name: "op_id", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "opwm_week", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "opwm_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230425-1623-008") {
    addForeignKeyConstraint(
      baseColumnNames: "opwm_month_fk",
      baseTableName: "omission_pattern_week_month",
      constraintName: "omission_pattern_week_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1681463077388-9") {
    createTable(tableName: "combination") {
      column(name: "c_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "c_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "c_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
        }
    }
  
  changeSet(author: "jack-golding (generated)", id: "1681463077388-18") {
    createTable(tableName: "combination_rule") {
      column(name: "cr_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cr_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "cr_time_unit_fk", type: "VARCHAR(36)") {constraints(nullable: "false") }
      column(name: "cr_issues_to_combine", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "cr_pattern_type_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cr_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "jack-golding (generated)", id: "1681463077388-10") {
    createTable(tableName: "combination_pattern") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cp_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "cp_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "jack-golding (generated)", id: "1681463077388-12") {
    createTable(tableName: "combination_pattern_month") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpm_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "jack-golding (generated)", id: "1681463077388-14") {
    createTable(tableName: "combination_pattern_week") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpw_week", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230414-1110-001") {
    addForeignKeyConstraint(
      baseColumnNames: "c_owner_fk",
      baseTableName: "combination",
      constraintName: "combination_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "sr_id",
      referencedTableName: "serial_ruleset"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230414-1110-002") {
    addUniqueConstraint(columnNames: "c_id", constraintName: "c_id_unique", tableName: "combination")
    addForeignKeyConstraint(
      baseColumnNames: "cr_owner_fk",
      baseTableName: "combination_rule",
      constraintName: "combination_rule_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "c_id",
      referencedTableName: "combination"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230414-1110-003") {
    addUniqueConstraint(columnNames: "cr_id", constraintName: "cr_id_unique", tableName: "combination_rule")
    addForeignKeyConstraint(
      baseColumnNames: "cp_owner_fk",
      baseTableName: "combination_pattern",
      constraintName: "combination_pattern_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "cr_id",
      referencedTableName: "combination_rule"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230414-1110-004") {
    addForeignKeyConstraint(
      baseColumnNames: "cr_pattern_type_fk",
      baseTableName: "combination_rule",
      constraintName: "combination_rule_pattern_type_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230414-1110-005") {
    addForeignKeyConstraint(
      baseColumnNames: "cr_time_unit_fk",
      baseTableName: "combination_rule",
      constraintName: "combination_rule_time_unit_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230414-1110-006") {
    addForeignKeyConstraint(
      baseColumnNames: "cpm_month_fk",
      baseTableName: "combination_pattern_month",
      constraintName: "combination_pattern_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1683205908546-4") {
    createTable(tableName: "combination_pattern_day") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpd_day", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "jack-golding (generated)", id: "1683205908546-5") {
    createTable(tableName: "combination_pattern_day_month") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpdm_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpdm_day", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230504-1445-001") {
    addForeignKeyConstraint(
      baseColumnNames: "cpdm_month_fk",
      baseTableName: "combination_pattern_day_month",
      constraintName: "combination_pattern_day_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1683205908546-6") {
    createTable(tableName: "combination_pattern_day_week") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpdw_week", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "cpdw_weekday_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230504-1445-002") {
    addForeignKeyConstraint(
      baseColumnNames: "cpdw_weekday_fk",
      baseTableName: "combination_pattern_day_week",
      constraintName: "combination_pattern_day_week_weekday_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1683205908546-7") {
    createTable(tableName: "combination_pattern_day_week_month") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpdwm_week", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "cpdwm_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpdwm_weekday_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230504-1445-003") {
    addForeignKeyConstraint(
      baseColumnNames: "cpdwm_month_fk",
      baseTableName: "combination_pattern_day_week_month",
      constraintName: "combination_pattern_day_week_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230504-1445-004") {
    addForeignKeyConstraint(
      baseColumnNames: "cpdwm_weekday_fk",
      baseTableName: "combination_pattern_day_week_month",
      constraintName: "combination_pattern_day_week_month_weekday_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1683205908546-8") {
    createTable(tableName: "combination_pattern_day_weekday") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpdwd_weekday_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230504-1445-005") {
    addForeignKeyConstraint(
      baseColumnNames: "cpdwd_weekday_fk",
      baseTableName: "combination_pattern_day_weekday",
      constraintName: "combination_pattern_day_weekday_weekday_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1683205908546-9") {
    createTable(tableName: "combination_pattern_issue") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpi_issue", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "jack-golding (generated)", id: "1683205908546-10") {
    createTable(tableName: "combination_pattern_issue_month") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpim_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpim_issue", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230504-1445-006") {
    addForeignKeyConstraint(
      baseColumnNames: "cpim_month_fk",
      baseTableName: "combination_pattern_issue_month",
      constraintName: "combination_pattern_issue_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1683205908546-11") {
    createTable(tableName: "combination_pattern_issue_week") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpiw_week", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "cpiw_issue", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "jack-golding (generated)", id: "1683205908546-12") {
    createTable(tableName: "combination_pattern_issue_week_month") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false")            }
      column(name: "cpiwm_week", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "cpiwm_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpiwm_issue", type: "INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230504-1445-007") {
    addForeignKeyConstraint(
      baseColumnNames: "cpiwm_month_fk",
      baseTableName: "combination_pattern_issue_week_month",
      constraintName: "combination_pattern_issue_week_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "jack-golding (generated)", id: "1683205908546-13") {
    createTable(tableName: "combination_pattern_week_month") {
      column(name: "cp_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpwm_week", type: "INTEGER") { constraints(nullable: "false") }
      column(name: "cpwm_month_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230504-1445-008") {
    addForeignKeyConstraint(
      baseColumnNames: "cpwm_month_fk",
      baseTableName: "combination_pattern_week_month",
      constraintName: "combination_pattern_week_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230728-1227-001"){
    createTable(tableName: "internal_piece") {
      column(name: "ip_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "ip_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230728-1227-002"){
    createTable(tableName: "internal_recurrence_piece") {
      column(name: "ip_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "irp_date", type: "timestamp") { constraints(nullable: "false") }
      column(name: "irp_recurrence_rule_fk", type: "VARCHAR(36)") { constraints(nullable: "true") }
      // column(name: "irp_owner", type: "VARCHAR(36)") { constraints(nullable: "true") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230728-1227-003"){
    createTable(tableName: "internal_omission_piece") {
      column(name: "ip_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "iop_date", type: "timestamp") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230728-1227-004"){
    createTable(tableName: "internal_combination_piece") {
      column(name: "ip_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230731-1506-001"){
    createTable(tableName: "omission_origin") {
      column(name: "oo_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "oo_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "oo_omission_rule_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "oo_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230731-1506-002"){
    createTable(tableName: "cominbation_origin") {
      column(name: "co_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "co_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "co_combination_rule_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "co_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230816-1138-001") {
    addColumn(tableName: "serial_ruleset") {
      column(name: "sr_ruleset_number", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-001"){
    createTable(tableName: "template_config") {
      column(name: "tc_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "tc_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "tc_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "tc_template_string", type: "TEXT") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-002"){
    createTable(tableName: "template_metadata_rule") {
      column(name: "tmr_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "tmr_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "tmr_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "tmr_template_metadata_rule_type_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-003"){
    createTable(tableName: "template_metadata_rule_type") {
      column(name: "tmrt_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "tmrt_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "tmrt_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-004"){
    createTable(tableName: "chronology_template_metadata_rule") {
      column(name: "tmrt_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "ctmr_template_metadata_rule_format_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-005"){
    createTable(tableName: "enumeration_template_metadata_rule") {
      column(name: "tmrt_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "etmr_template_metadata_rule_format_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-006"){
    createTable(tableName: "template_metadata_rule_format") {
      column(name: "tmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "tmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "tmrf_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-007"){
    createTable(tableName: "chronology_datetmrf") {
      column(name: "tmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cdtmrf_weekday_format_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cdtmrf_month_day_format_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cdtmrf_month_format_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cdtmrf_year_format_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-008"){
    createTable(tableName: "chronology_monthtmrf") {
      column(name: "tmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cmtmrf_month_format_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cmtmrf_year_format_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-009"){
    createTable(tableName: "chronology_yeartmrf") {
      column(name: "tmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cytmrf_year_format_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-010"){
    createTable(tableName: "enumeration_numeric_leveltmrf") {
      column(name: "enltmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "enltmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "enltmrf_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "enltmrf_units", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "enltmrf_format_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "enltmrf_sequence_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "enltmrf_internal_note", type: "TEXT") { constraints(nullable: "true") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231001-1230-011"){
    createTable(tableName: "enumeration_textual_leveltmrf") {
      column(name: "etltmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "etltmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "etltmrf_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "etltmrf_units", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "etltmrf_value", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "etltmrf_internal_note", type: "TEXT") { constraints(nullable: "true") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231128-1315-001") {
    addColumn(tableName: "internal_piece") {
      column(name: "ip_template_string", type: "TEXT") { constraints(nullable: "true") }
      column(name: "ip_label", type: "TEXT") { constraints(nullable: "true") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231207-1230-001"){
    createTable(tableName: "enumeration_numerictmrf") {
      column(name: "tmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20231207-1230-002"){
    createTable(tableName: "enumeration_textualtmrf") {
      column(name: "tmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }
}
