package de.hdm_stuttgart.mi.read.model;

import java.util.ArrayList;

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
public record Table(String name, ArrayList<Column> columns, ArrayList<FkRelation> importedFkRelations,
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
}
