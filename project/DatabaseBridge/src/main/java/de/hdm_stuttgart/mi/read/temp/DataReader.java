package de.hdm_stuttgart.mi.read.temp;

import javax.sql.rowset.CachedRowSet;

public interface DataReader {
    /**
     * Read data of a single table
     *
     * @param tableName the name of the table which the data should be read from
     * @return CashedRowSet containing all records of a table
     */
    CachedRowSet readTableData(String tableName);
}
