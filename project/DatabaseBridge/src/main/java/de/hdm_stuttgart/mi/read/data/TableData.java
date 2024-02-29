package de.hdm_stuttgart.mi.read.data;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;

public record TableData(String schemaName, String tableName, ArrayList<String> columns, CachedRowSet data) {
}
