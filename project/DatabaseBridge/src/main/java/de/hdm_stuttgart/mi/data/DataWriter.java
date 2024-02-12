package de.hdm_stuttgart.mi.data;

import de.hdm_stuttgart.mi.read.model.Schema;
import de.hdm_stuttgart.mi.read.model.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataWriter {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    final private Schema schema;
    final private Connection connection;

    final private ArrayList<ResultSet> tables;

    /**
     * Writes data records to tables
     *
     * @param schema structure of the tables
     * @param connection database to write the records in
     * @param tables data records of tables
     */
    public DataWriter(Schema schema, Connection connection, ArrayList<ResultSet> tables) {
        this.schema = schema;
        this.connection = connection;
        this.tables = tables;
    }


    /**
     * write the provided table data, into the provided database
     * @return true if successful, false otherwise
     */
    public boolean writeTableData() {
        //Iterate through tables
        for(int tableIndex = 0; tableIndex < schema.tables().size(); tableIndex++) {
            Table table = schema.tables().get(tableIndex);
            ArrayList<String> columns = new ArrayList<>();

            //Get column names of current table
            for(int columnIndex = 0; columnIndex < table.columns().size(); columnIndex++) {
                columns.add(schema.tables().get(tableIndex).columns().get(columnIndex).name());
            }

            try (Statement stmt = connection.createStatement()) {
            // Iterate over the ResultSet and construct INSERT statement
            while (tables.get(tableIndex).next()) {
                StringBuilder values = new StringBuilder();
                for (int i = 1; i <= columns.size(); i++) {
                    Object value = tables.get(tableIndex).getObject(i);
                    //Check which type the current column has
                    if (value == null) {
                        values.append("NULL");
                    } else if (value instanceof Number) {
                        values.append(value);
                    } else {
                        values.append("'").append(value).append("'");
                    }
                    if (i < columns.size()) {
                        values.append(", ");
                    }

                }
                // Construct the INSERT statement
                String insertQuery = "INSERT INTO " + schema.name() + '.' + schema.tables().get(tableIndex).name() +
                        " VALUES (" + values + ")";
                stmt.executeUpdate(insertQuery);

            }
            //Execution together
            } catch (SQLException sqlException) {
                log.log(Level.SEVERE, "SQLException while writing table data: " + sqlException.getMessage());
                return false;
            }

            //Close ResultSet
            try {
                tables.get(tableIndex).close();
            } catch (SQLException sqlException) {
                log.log(Level.SEVERE, "SQLException while writing table data: " + sqlException.getMessage());
                return false;
            }
        }
        return true;
    }


    //TODO: Better use preparedStatement, but all types need to be checked
    private String createPreparedStatement(String tableName, ArrayList<String> columnNames) {
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
