package de.hdm_stuttgart.mi.write.schema.temp;

import de.hdm_stuttgart.mi.read.model.Schema;
import de.hdm_stuttgart.mi.read.model.Table;


public class DataWriter {
    public void writeData(Schema schema){

        for (Table table : schema.tables()) {
            System.out.println(DataStatementBuilder.dataAsStatement(table));
        }


    }
}
