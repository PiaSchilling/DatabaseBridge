package de.hdm_stuttgart.mi.read.api;

import de.hdm_stuttgart.mi.read.model.Table;

public interface TableReader {
    /**
     * Read a single table
     *
     * @param tableName the name of the table which should be read
     * @return a table object containing columns and other table attributes
     */
    Table readTable(String tableName);
}
