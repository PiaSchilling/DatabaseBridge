package de.hdm_stuttgart.mi.write.data.implementation;

import de.hdm_stuttgart.mi.read.data.model.TableData;
import de.hdm_stuttgart.mi.write.data.api.DataWriter;

import java.util.ArrayList;


public class DataWriterImpl implements DataWriter {
    @Override
    public void writeData(ArrayList<TableData> data) {
        for (TableData tableData : data) {
            ArrayList<String> statements = DataStatementBuilder.dataAsStatement(tableData);
            if (statements != null) {
                for (String statement : statements) {
                    System.out.println(statement);
                }
            }

        }
    }
}
