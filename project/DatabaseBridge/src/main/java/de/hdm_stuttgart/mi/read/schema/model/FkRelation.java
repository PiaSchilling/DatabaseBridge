package de.hdm_stuttgart.mi.read.schema.model;

/**
 * Models a foreign key relationship to a column of a table
 *
 * @param tableName             the name of the foreign table this FkRelation references
 * @param referencedColumnName  the name of the column this FkRelation references in the foreign table
 * @param referencingColumnName the name of the column this FkRelation references in the table which defines
 *                              this fk relation
 * @param fkName                the name of the foreign key
 * @param updateRule            what happens to a foreign key when the primary key is updated
 * @param deleteRule            What happens to a foreign key when the primary key is deleted
 */
public record FkRelation(String tableName,
                         String referencedColumnName,
                         String referencingColumnName,
                         String fkName,
                         DeleteUpdateRule updateRule,
                         DeleteUpdateRule deleteRule) {
}
