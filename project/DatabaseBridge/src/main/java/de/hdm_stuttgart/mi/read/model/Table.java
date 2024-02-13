package de.hdm_stuttgart.mi.read.model;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Model class for a single database table
 *
 * @param name                the name of this table
 * @param columns             the columns of this table
 * @param importedFkRelations the foreign key relations where this table references other tables primary keys
 *                            (this table is the child)
 * @param exportedFkRelations the foreign key relations where this tables primary key is referenced by other tables
 *                            (this table is the parent)
 */
public record Table(String name, ArrayList<Column> columns, CachedRowSet data, ArrayList<FkRelation> importedFkRelations,
                    ArrayList<FkRelation> exportedFkRelations) {

    /**
     * Check if this table is a parent table which means its referenced by other tables via FK relations
     *
     * @return true if it is referenced by other tables, false if not
     */
    public boolean isParentTable() {
        return exportedFkRelations.size() > 0;
    }

    /**
     * Check if this table is a child table which means it references other tables via FK relations
     *
     * @return true if it references other tables, false if not
     */
    public boolean isChildTable() {
        return importedFkRelations.size() > 0;
    }

    /**
     * Get this table as CREATE TABLE statement representation
     * @example {@code CREATE TABLE departments(dept_no CHAR(4) NOT NULL UNIQUE PRIMARY KEY,dept_name VARCHAR(40) NOT NULL UNIQUE);}
     * @return a SQL statement string which contains all attributes of this table
     */
    public String asStatement() {
        final String columnString = columns.stream().map(Column::asStatement).collect(Collectors.joining());
        final String fkString = importedFkRelations.stream().map(FkRelation::asStatement).collect(Collectors.joining());

        String createTableString = "CREATE TABLE " + name + "(" + columnString + fkString;

        if (createTableString.endsWith(",")) {
            createTableString = createTableString.substring(0, createTableString.lastIndexOf(","));
        }

        return createTableString + ");";
    }
}
