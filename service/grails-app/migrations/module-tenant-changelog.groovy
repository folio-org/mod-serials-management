databaseChangeLog = {
  include file: 'initial-customisations.groovy'
  include file: 'create-mod-serials-management.groovy'
  include file: 'update-mod-serials-management-1-1.groovy'
  include file: 'update-mod-serials-management-1-2.groovy'
  include file: 'add-missing-primary-keys-for-trillium.groovy'
}
