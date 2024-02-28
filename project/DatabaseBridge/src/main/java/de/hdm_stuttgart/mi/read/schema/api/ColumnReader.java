package de.hdm_stuttgart.mi.read.schema.api;

import de.hdm_stuttgart.mi.read.schema.model.Column;

import java.util.ArrayList;

public interface ColumnReader {
    /**
     * Read all columns from a table
     *
     * @param tableName the name of the table the columns should be read of
     * @param schemaName the name of the schema the table belongs to
     * @return a list of columns which belong to the table
     */
    ArrayList<Column> readTableColumns(String tableName, String schemaName);
}
