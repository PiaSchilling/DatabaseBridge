package de.hdm_stuttgart.mi.data;

import de.hdm_stuttgart.mi.read.model.Schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ReadData {
    final private Schema schema;
    final private Connection connection;

    public ReadData(Schema schema, Connection connection) {
        this.schema = schema;
        this.connection = connection;
    }

    public ArrayList<ResultSet> readTableData() {
        ArrayList<ResultSet> tables = new ArrayList<>();
        //Get all tables in schema
        try{
            for(int i = 0; i < schema.tables().size(); i++) {
                final Statement stmt = connection.createStatement();
                tables.add(stmt.executeQuery("SELECT * FROM " + schema.name() + '.' + schema.tables().get(i).name()));
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return tables;
    }
}
