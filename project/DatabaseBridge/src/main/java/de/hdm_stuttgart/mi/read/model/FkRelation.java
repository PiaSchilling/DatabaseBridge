package de.hdm_stuttgart.mi.read.model;

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

    /**
     * Get this relation as statement representation which can be used to build the relations in a CREATE TABLE statement
     * @example {@code REFERENCES employees(emp_no) ON UPDATE RESTRICT ON DELETE CASCADE}
     * @return a SQL statement string containing all attributes of this fk relation
     */
    public String asStatement() {
        return "FOREIGN KEY(" + referencingColumnName + ")"
                + " REFERENCES " + tableName + "(" + referencedColumnName
                + ") ON UPDATE " + updateRule.asString
                + " ON DELETE " + deleteRule.asString
                + ",";
    }
}
