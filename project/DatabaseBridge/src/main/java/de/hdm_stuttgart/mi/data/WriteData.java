package de.hdm_stuttgart.mi.data;

import de.hdm_stuttgart.mi.read.model.Schema;
import de.hdm_stuttgart.mi.read.model.Table;

import java.sql.*;
import java.util.ArrayList;

public class WriteData {
    final private Schema schema;
    final private Connection connection;

    final private ArrayList<ResultSet> tables;

    public WriteData(Schema schema, Connection connection, ArrayList<ResultSet> tables) {
        this.schema = schema;
        this.connection = connection;
        this.tables = tables;
    }

    //Test
    //Improvements with try error handling

    public boolean writeTableData() {
        try{
        //Iterate through tables
        for(int tableIndex = 0; tableIndex < schema.tables().size(); tableIndex++) {
            Table table = schema.tables().get(tableIndex);
            ArrayList<String> columns = new ArrayList<>();
            //Get column names of current table
            for(int columnIndex = 0; columnIndex < table.columns().size(); columnIndex++) {
                columns.add(schema.tables().get(tableIndex).columns().get(columnIndex).name());
            }

            Statement stmt = connection.createStatement();
            // Iterate over the ResultSet and construct INSERT statement
            while (tables.get(tableIndex).next()) {
                StringBuilder values = new StringBuilder();
                for (int i = 1; i <= columns.size(); i++) {
                    Object value = tables.get(tableIndex).getObject(i);
                    //Check which type the current column has
                    if (value == null) {
                        //preparedStatement.setNull(i, Types.NULL);
                        values.append("NULL");
                    } else if (value instanceof Number) {
                        //preparedStatement.setObject(i, value);
                        values.append(value);
                    } else {
                        //preparedStatement.setString(i, value.toString());
                        values.append("'").append(value).append("'");
                    }

                    //Limit Batch size?
                    if (i < columns.size()) {
                        values.append(", ");
                    }
                }
                // Construct the INSERT statement
                String insertQuery = "INSERT INTO " + schema.name() + '.' + schema.tables().get(tableIndex).name() +
                        " VALUES (" + values + ")";

               stmt.executeUpdate(insertQuery);
            }

        }


        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    private String createPreparedStatement(String tableName, ArrayList<String> columnNames) {
        // Dynamically create the INSERT statement based on column names
        StringBuilder insertQueryBuilder = new StringBuilder("INSERT INTO " + schema.name() + "." + tableName + " (");
        for (int i = 0; i < columnNames.size(); i++) {
            insertQueryBuilder.append(columnNames.get(i));
            if (i < columnNames.size() - 1) {
                insertQueryBuilder.append(", ");
            }
        }
        insertQueryBuilder.append(") VALUES (");

        for (int i = 0; i < columnNames.size(); i++) {
            insertQueryBuilder.append("?");
            if (i < columnNames.size() - 1) {
                insertQueryBuilder.append(", ");
            }
        }
        insertQueryBuilder.append(")");

        return  insertQueryBuilder.toString();
    }
}
