databaseChangeLog = {
  changeSet(author: "Jack-Golding (manual)", id: "20240416-1325-001") {
    addColumn(tableName: "chronology_template_metadata_rule") {
      column(name: "ctmr_rule_locale", type: "VARCHAR(36)") { constraints(nullable: "true") }
    }
  }
}