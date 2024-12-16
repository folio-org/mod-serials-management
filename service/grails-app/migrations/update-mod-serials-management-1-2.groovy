databaseChangeLog = {
  changeSet(author: "Jack-Golding (manual)", id: "20241216-1339-001") {
    addColumn(tableName: "predicted_piece_set") {
      column(name: "pps_number_of_cycles", type: "INTEGER") { constraints(nullable: "true") }
    }
  }
}