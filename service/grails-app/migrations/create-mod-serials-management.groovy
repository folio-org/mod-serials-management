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
      column(name: "or_pattern_type_fk", type: "VARCHAR(36)")
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
      baseColumnNames: "or_pattern_type_fk",
      baseTableName: "omission_rule",
      constraintName: "omission_rule_pattern_type_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-006") {
    createTable(tableName: "omission_pattern") {
      column(name: "op_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "op_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "op_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-007") {
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

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-008") {
    createTable(tableName: "omission_pattern_days_in_month") {
      column(name: "op_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opdim_day", type: "INT")
      column(name: "opdim_month_fk", type: "VARCHAR(36)")    
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1237-009") {
    addForeignKeyConstraint(
      baseColumnNames: "opdim_month_fk",
      baseTableName: "omission_pattern_days_in_month",
      constraintName: "omission_pattern_days_in_month_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1545-001") {
    createTable(tableName: "omission_pattern_nth_issue") {
      column(name: "op_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "opni_issue", type: "INT")
      column(name: "opni_month_fk", type: "VARCHAR(36)")    
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20230403-1545-002") {
    addForeignKeyConstraint(
      baseColumnNames: "opni_month_fk",
      baseTableName: "omission_pattern_nth_issue",
      constraintName: "omission_pattern_nth_issue_month_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }
}
