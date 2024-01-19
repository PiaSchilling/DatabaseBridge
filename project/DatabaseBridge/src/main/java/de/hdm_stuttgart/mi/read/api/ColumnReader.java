package de.hdm_stuttgart.mi.read.api;

import de.hdm_stuttgart.mi.read.model.Column;

import java.util.ArrayList;

public interface ColumnReader {
    /**
     * Read all columns from a table
     *
     * @param tableName the name of the table the columns should be read of
     * @return a list of columns which belong to the the
     */
    ArrayList<Column> readTableColumns(String tableName);
}
