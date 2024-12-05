databaseChangeLog = {
  // With the removal of TemplateMetadataRule abstract class. both ChronologyTMR and EnumerationTMR will now need to store their own id, owner, version and index
  
  // ENUMERATION CHANGES
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-001") {
    addColumn(tableName: "enumeration_template_metadata_rule") {
      column(name: "etmr_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "etmr_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "etmr_index", type:"INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-002") {
    renameColumn(tableName: "enumeration_template_metadata_rule", oldColumnName: "tmrt_id", newColumnName: "etmr_id")
  }

  // CHRONOLOGY CHANGES
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-003") {
    addColumn(tableName: "chronology_template_metadata_rule") {
      column(name: "ctmr_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "ctmr_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "ctmr_index", type:"INTEGER") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-004") {
    renameColumn(tableName: "chronology_template_metadata_rule", oldColumnName: "tmrt_id", newColumnName: "ctmr_id")
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
  
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-005") {
    createTable(tableName: "enumeration_template_metadata_rule_format") {
      column(name: "etmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "etmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "etmrf_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-006") {
    createTable(tableName: "chronology_template_metadata_rule_format") {
      column(name: "ctmrf_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "ctmrf_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "ctmrf_version", type: "BIGINT") { constraints(nullable: "false") }
    }
  }

  //Update enumeration TMRF tables "tmrf_id" to match new ETMRF "etmrf_id"
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-007") {
    renameColumn(tableName: "enumeration_numerictmrf", oldColumnName: "tmrf_id", newColumnName: "etmrf_id")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-008") {
    renameColumn(tableName: "enumeration_textualtmrf", oldColumnName: "tmrf_id", newColumnName: "etmrf_id")
  }

  //Update chronology TMRF tables "tmrf_id" to match new CTMRF "Ctmrf_id"
  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-009") {
    renameColumn(tableName: "chronology_datetmrf", oldColumnName: "tmrf_id", newColumnName: "ctmrf_id")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-010") {
    renameColumn(tableName: "chronology_monthtmrf", oldColumnName: "tmrf_id", newColumnName: "ctmrf_id")
  }

  changeSet(author: "Jack-Golding (manual)", id: "20241205-1222-011") {
    renameColumn(tableName: "chronology_yeartmrf", oldColumnName: "tmrf_id", newColumnName: "ctmrf_id")
  }
}