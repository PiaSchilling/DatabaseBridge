package de.hdm_stuttgart.mi.read.api;

import de.hdm_stuttgart.mi.read.model.Table;

public interface TableReader {
    /**
     * Read a single table
     *
     * @param tableName the name of the table which should be read
     * @param schemaName the name of the schema the table belongs to
     * @return a table object containing columns and other table attributes
     */
    Table readTable(String tableName,String schemaName);
}
