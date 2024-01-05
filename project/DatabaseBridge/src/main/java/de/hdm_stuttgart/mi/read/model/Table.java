package de.hdm_stuttgart.mi.read.model;

import java.util.ArrayList;

public class Table {
    private final String name;
    private final ArrayList<Column> columns;

    private final ArrayList<FkRelation> importedFkRelations;

    private final ArrayList<FkRelation> exportedFkRelations;

    /**
     * @param name                the name of this table
     * @param columns             the columns of this table
     * @param importedFkRelations the foreign key relations where this table references other tables primary keys
     *                            (this table is the child)
     * @param exportedFkRelations the foreign key relations where this tables primary key is referenced by other tables
     *                            (this table is the parent)
     */
    public Table(String name, ArrayList<Column> columns, ArrayList<FkRelation> importedFkRelations, ArrayList<FkRelation> exportedFkRelations) {
        this.name = name;
        this.columns = columns;
        this.importedFkRelations = importedFkRelations;
        this.exportedFkRelations = exportedFkRelations;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", columns=" + columns +
                ", importedFkRelations=" + importedFkRelations +
                ", exportedFkRelations=" + exportedFkRelations +
                '}';
    }

    public String getName() {
        return name;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public ArrayList<FkRelation> getImportedFkRelations() {
        return importedFkRelations;
    }

    public ArrayList<FkRelation> getExportedFkRelations() {
        return exportedFkRelations;
    }
}
