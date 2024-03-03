package de.hdm_stuttgart.mi.read.data.model;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;

/**
 * Model class for a single database table includes data
 *
 * @param schemaName the name of the schema the table belongs to
 * @param tableName the name of this table
 * @param columns the columns of this table
 * @param data the data of this table
 */
public record TableData(String schemaName, String tableName, ArrayList<String> columns, CachedRowSet data) {
}
