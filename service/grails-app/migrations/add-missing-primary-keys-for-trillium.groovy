databaseChangeLog = {
  changeSet(author: "mchaib (manual)", id: "20250702-1515-01") {
    preConditions(onFail: 'MARK_RAN') {
      not { primaryKeyExists(tableName: 'omission_pattern_day_week') }
    }
    addPrimaryKey(tableName: "omission_pattern_day_week", columnNames: "op_id", constraintName: "omission_pattern_day_weekPK")
  }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-02") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'chronology_datetmrf') }
      }
      addPrimaryKey(tableName: "chronology_datetmrf", columnNames: "ctmrf_id", constraintName: "chronology_datetmrfPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-03") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'chronology_monthtmrf') }
      }
      addPrimaryKey(tableName: "chronology_monthtmrf", columnNames: "ctmrf_id", constraintName: "chronology_monthtmrfPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-04") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'chronology_template_metadata_rule_format') }
      }
      addPrimaryKey(tableName: "chronology_template_metadata_rule_format", columnNames: "ctmrf_id", constraintName: "chronology_template_metadata_rule_formatPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-05") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'chronology_yeartmrf') }
      }
      addPrimaryKey(tableName: "chronology_yeartmrf", columnNames: "ctmrf_id", constraintName: "chronology_yeartmrfPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-06") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'chronologyuctmt') }
      }
      addPrimaryKey(tableName: "chronologyuctmt", columnNames: "uctmt_id", constraintName: "chronologyuctmtPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-07") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination') }
      }
      addPrimaryKey(tableName: "combination", columnNames: "c_id", constraintName: "combinationPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-08") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_origin') }
      }
      addPrimaryKey(tableName: "combination_origin", columnNames: "co_id", constraintName: "combination_originPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-09") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern') }
      }
      addPrimaryKey(tableName: "combination_pattern", columnNames: "cp_id", constraintName: "combination_patternPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-10") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_day') }
      }
      addPrimaryKey(tableName: "combination_pattern_day", columnNames: "cp_id", constraintName: "combination_pattern_dayPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-11") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_day_month') }
      }
      addPrimaryKey(tableName: "combination_pattern_day_month", columnNames: "cp_id", constraintName: "combination_pattern_day_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-12") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_day_week') }
      }
      addPrimaryKey(tableName: "combination_pattern_day_week", columnNames: "cp_id", constraintName: "combination_pattern_day_weekPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-13") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_day_week_month') }
      }
      addPrimaryKey(tableName: "combination_pattern_day_week_month", columnNames: "cp_id", constraintName: "combination_pattern_day_week_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-14") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_day_weekday') }
      }
      addPrimaryKey(tableName: "combination_pattern_day_weekday", columnNames: "cp_id", constraintName: "combination_pattern_day_weekdayPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-15") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_issue') }
      }
      addPrimaryKey(tableName: "combination_pattern_issue", columnNames: "cp_id", constraintName: "combination_pattern_issuePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-16") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_issue_month') }
      }
      addPrimaryKey(tableName: "combination_pattern_issue_month", columnNames: "cp_id", constraintName: "combination_pattern_issue_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-17") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_issue_week') }
      }
      addPrimaryKey(tableName: "combination_pattern_issue_week", columnNames: "cp_id", constraintName: "combination_pattern_issue_weekPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-18") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_issue_week_month') }
      }
      addPrimaryKey(tableName: "combination_pattern_issue_week_month", columnNames: "cp_id", constraintName: "combination_pattern_issue_week_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-19") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_month') }
      }
      addPrimaryKey(tableName: "combination_pattern_month", columnNames: "cp_id", constraintName: "combination_pattern_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-20") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_week') }
      }
      addPrimaryKey(tableName: "combination_pattern_week", columnNames: "cp_id", constraintName: "combination_pattern_weekPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-21") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_pattern_week_month') }
      }
      addPrimaryKey(tableName: "combination_pattern_week_month", columnNames: "cp_id", constraintName: "combination_pattern_week_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-22") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'combination_rule') }
      }
      addPrimaryKey(tableName: "combination_rule", columnNames: "cr_id", constraintName: "combination_rulePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-23") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'enumeration_leveluctmt') }
      }
      addPrimaryKey(tableName: "enumeration_leveluctmt", columnNames: "eluctmt_id", constraintName: "enumeration_leveluctmtPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-24") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'enumeration_numeric_leveltmrf') }
      }
      addPrimaryKey(tableName: "enumeration_numeric_leveltmrf", columnNames: "enltmrf_id", constraintName: "enumeration_numeric_leveltmrfPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-25") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'enumeration_numerictmrf') }
      }
      addPrimaryKey(tableName: "enumeration_numerictmrf", columnNames: "etmrf_id", constraintName: "enumeration_numerictmrfPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-26") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'enumeration_template_metadata_rule_format') }
      }
      addPrimaryKey(tableName: "enumeration_template_metadata_rule_format", columnNames: "etmrf_id", constraintName: "enumeration_template_metadata_rule_formatPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-27") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'enumeration_textual_leveltmrf') }
      }
      addPrimaryKey(tableName: "enumeration_textual_leveltmrf", columnNames: "etltmrf_id", constraintName: "enumeration_textual_leveltmrfPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-28") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'enumeration_textualtmrf') }
      }
      addPrimaryKey(tableName: "enumeration_textualtmrf", columnNames: "etmrf_id", constraintName: "enumeration_textualtmrfPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-29") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'enumerationuctmt') }
      }
      addPrimaryKey(tableName: "enumerationuctmt", columnNames: "uctmt_id", constraintName: "enumerationuctmtPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-30") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'internal_combination_piece') }
      }
      addPrimaryKey(tableName: "internal_combination_piece", columnNames: "ip_id", constraintName: "internal_combination_piecePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-31") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'internal_combination_piece_internal_recurrence_piece') }
      }
      addPrimaryKey(tableName: "internal_combination_piece_internal_recurrence_piece", columnNames: "internal_combination_piece_recurrence_pieces_id", constraintName: "internal_combination_piece_internal_recurrence_piecePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-32") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'internal_omission_piece') }
      }
      addPrimaryKey(tableName: "internal_omission_piece", columnNames: "ip_id", constraintName: "internal_omission_piecePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-33") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'internal_piece') }
      }
      addPrimaryKey(tableName: "internal_piece", columnNames: "ip_id", constraintName: "internal_piecePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-34") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'internal_recurrence_piece') }
      }
      addPrimaryKey(tableName: "internal_recurrence_piece", columnNames: "ip_id", constraintName: "internal_recurrence_piecePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-35") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'model_ruleset') }
      }
      addPrimaryKey(tableName: "model_ruleset", columnNames: "mr_id", constraintName: "model_rulesetPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-36") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission') }
      }
      addPrimaryKey(tableName: "omission", columnNames: "o_id", constraintName: "omissionPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-37") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_origin') }
      }
      addPrimaryKey(tableName: "omission_origin", columnNames: "oo_id", constraintName: "omission_originPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-38") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern') }
      }
      addPrimaryKey(tableName: "omission_pattern", columnNames: "op_id", constraintName: "omission_patternPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-39") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_day') }
      }
      addPrimaryKey(tableName: "omission_pattern_day", columnNames: "op_id", constraintName: "omission_pattern_dayPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-40") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_day_month') }
      }
      addPrimaryKey(tableName: "omission_pattern_day_month", columnNames: "op_id", constraintName: "omission_pattern_day_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-41") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_day_week') }
      }
      addPrimaryKey(tableName: "omission_pattern_day_week", columnNames: "op_id", constraintName: "omission_pattern_day_weekPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-42") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_day_week_month') }
      }
      addPrimaryKey(tableName: "omission_pattern_day_week_month", columnNames: "op_id", constraintName: "omission_pattern_day_week_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-43") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_day_weekday') }
      }
      addPrimaryKey(tableName: "omission_pattern_day_weekday", columnNames: "op_id", constraintName: "omission_pattern_day_weekdayPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-44") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_issue') }
      }
      addPrimaryKey(tableName: "omission_pattern_issue", columnNames: "op_id", constraintName: "omission_pattern_issuePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-45") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_issue_month') }
      }
      addPrimaryKey(tableName: "omission_pattern_issue_month", columnNames: "op_id", constraintName: "omission_pattern_issue_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-46") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_issue_week') }
      }
      addPrimaryKey(tableName: "omission_pattern_issue_week", columnNames: "op_id", constraintName: "omission_pattern_issue_weekPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-47") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_issue_week_month') }
      }
      addPrimaryKey(tableName: "omission_pattern_issue_week_month", columnNames: "op_id", constraintName: "omission_pattern_issue_week_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-48") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_month') }
      }
      addPrimaryKey(tableName: "omission_pattern_month", columnNames: "op_id", constraintName: "omission_pattern_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-49") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_week') }
      }
      addPrimaryKey(tableName: "omission_pattern_week", columnNames: "op_id", constraintName: "omission_pattern_weekPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-50") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_pattern_week_month') }
      }
      addPrimaryKey(tableName: "omission_pattern_week_month", columnNames: "op_id", constraintName: "omission_pattern_week_monthPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-51") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'omission_rule') }
      }
      addPrimaryKey(tableName: "omission_rule", columnNames: "or_id", constraintName: "omission_rulePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-52") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'predicted_piece_set') }
      }
      addPrimaryKey(tableName: "predicted_piece_set", columnNames: "pps_id", constraintName: "predicted_piece_setPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-53") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'receiving_piece') }
      }
      addPrimaryKey(tableName: "receiving_piece", columnNames: "recp_id", constraintName: "receiving_piecePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-54") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'recurrence') }
      }
      addPrimaryKey(tableName: "recurrence", columnNames: "r_id", constraintName: "recurrencePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-55") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'recurrence_pattern') }
      }
      addPrimaryKey(tableName: "recurrence_pattern", columnNames: "rp_id", constraintName: "recurrence_patternPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-56") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'recurrence_pattern_day') }
      }
      addPrimaryKey(tableName: "recurrence_pattern_day", columnNames: "rp_id", constraintName: "recurrence_pattern_dayPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-57") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'recurrence_pattern_month_date') }
      }
      addPrimaryKey(tableName: "recurrence_pattern_month_date", columnNames: "rp_id", constraintName: "recurrence_pattern_month_datePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-58") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'recurrence_pattern_month_weekday') }
      }
      addPrimaryKey(tableName: "recurrence_pattern_month_weekday", columnNames: "rp_id", constraintName: "recurrence_pattern_month_weekdayPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-59") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'recurrence_pattern_week') }
      }
      addPrimaryKey(tableName: "recurrence_pattern_week", columnNames: "rp_id", constraintName: "recurrence_pattern_weekPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-60") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'recurrence_pattern_year_date') }
      }
      addPrimaryKey(tableName: "recurrence_pattern_year_date", columnNames: "rp_id", constraintName: "recurrence_pattern_year_datePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-61") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'recurrence_pattern_year_month_weekday') }
      }
      addPrimaryKey(tableName: "recurrence_pattern_year_month_weekday", columnNames: "rp_id", constraintName: "recurrence_pattern_year_month_weekdayPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-62") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'recurrence_pattern_year_weekday') }
      }
      addPrimaryKey(tableName: "recurrence_pattern_year_weekday", columnNames: "rp_id", constraintName: "recurrence_pattern_year_weekdayPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-63") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'recurrence_rule') }
      }
      addPrimaryKey(tableName: "recurrence_rule", columnNames: "rr_id", constraintName: "recurrence_rulePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-64") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'ruleset_owner') }
      }
      addPrimaryKey(tableName: "ruleset_owner", columnNames: "ro_id", constraintName: "ruleset_ownerPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-65") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'serial') }
      }
      addPrimaryKey(tableName: "serial", columnNames: "s_id", constraintName: "serialPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-66") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'serial_note') }
      }
      addPrimaryKey(tableName: "serial_note", columnNames: "sn_id", constraintName: "serial_notePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-67") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'serial_order_line') }
      }
      addPrimaryKey(tableName: "serial_order_line", columnNames: "rol_id", constraintName: "serial_order_linePK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-68") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'serial_ruleset') }
      }
      addPrimaryKey(tableName: "serial_ruleset", columnNames: "sr_id", constraintName: "serial_rulesetPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-69") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'standard_template_metadata') }
      }
      addPrimaryKey(tableName: "standard_template_metadata", columnNames: "stm_id", constraintName: "standard_template_metadataPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-70") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'template_config') }
      }
      addPrimaryKey(tableName: "template_config", columnNames: "tc_id", constraintName: "template_configPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-71") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'template_metadata') }
      }
      addPrimaryKey(tableName: "template_metadata", columnNames: "tm_id", constraintName: "template_metadataPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-73") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'user_configured_template_metadata') }
      }
      addPrimaryKey(tableName: "user_configured_template_metadata", columnNames: "uctm_id", constraintName: "user_configured_template_metadataPK")
    }

    changeSet(author: "mchaib (manual)", id: "20250702-1515-74") {
      preConditions(onFail: 'MARK_RAN') {
        not { primaryKeyExists(tableName: 'user_configured_template_metadata_type') }
      }
      addPrimaryKey(tableName: "user_configured_template_metadata_type", columnNames: "uctmt_id", constraintName: "user_configured_template_metadata_typePK")
    }
  }

