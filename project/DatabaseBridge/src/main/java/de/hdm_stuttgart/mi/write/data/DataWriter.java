package de.hdm_stuttgart.mi.write.data;

import de.hdm_stuttgart.mi.read.data.TableData;

import java.util.ArrayList;


public class DataWriter {
    public void writeData(ArrayList<TableData> data){
        for (TableData tableData : data) {
            ArrayList<String> statements = DataStatementBuilder.dataAsStatement(tableData);
            for(String statement : statements) {
                System.out.println(statement);
            }
        }
    }
}
