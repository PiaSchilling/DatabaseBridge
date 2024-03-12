package de.hdm_stuttgart.mi.read.data.api;

import de.hdm_stuttgart.mi.read.data.model.TableData;
import de.hdm_stuttgart.mi.read.schema.model.Schema;

import java.util.ArrayList;

public interface DataReader {

    /**
     * Read all table data of a single schema
     *
     * @param schema the schema the data should be read from
     * @return ArrayList containing all data of all tables in the schema
     */
    ArrayList<TableData> readData(Schema schema);
}
