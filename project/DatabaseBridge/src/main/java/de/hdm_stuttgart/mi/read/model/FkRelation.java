package de.hdm_stuttgart.mi.read.model;

/**
 * Models a foreign key relationship to a column of a table
 *
 * @param tableName  the name of the table this FkRelation references
 * @param columnName the name of the column this FkRelation references
 * @param fkName     the name of the foreign key
 * @param updateRule what happens to a foreign key when the primary key is updated
 * @param deleteRule What happens to a foreign key when the primary key is deleted
 */
public record FkRelation(String tableName, String columnName, String fkName, DeleteUpdateRule updateRule,
                         DeleteUpdateRule deleteRule) {
}
