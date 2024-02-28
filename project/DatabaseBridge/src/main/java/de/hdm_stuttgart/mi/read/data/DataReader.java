package de.hdm_stuttgart.mi.read.data;

import de.hdm_stuttgart.mi.read.schema.model.Schema;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;

public interface DataReader {

    ArrayList<TableData> readData(Schema schema);
}
