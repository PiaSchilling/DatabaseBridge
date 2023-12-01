package de.hdm_stuttgart.mi.read.implementation;

import de.hdm_stuttgart.mi.connect.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.DatabaseSystem;
import de.hdm_stuttgart.mi.connect.SourceConnectionHandler;
import de.hdm_stuttgart.mi.read.model.Column;
import de.hdm_stuttgart.mi.read.model.Schema;
import de.hdm_stuttgart.mi.read.model.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SchemaReader {

    private final DatabaseMetaData metaData;

    public SchemaReader(Connection connection) {
        try {
            this.metaData = connection.getMetaData();
        } catch (SQLException e) {
            System.out.println("Not able to extract metadata from connection"); // TODO replace with logger
            throw new RuntimeException(e);
        }
    }

    public Schema readSchema() {
        final ArrayList<String> tableNames = readTableNames();
        final ArrayList<Table> tables = readTableColumns(tableNames);
        return new Schema(tables);
    }


    private ArrayList<String> readTableNames() {
        ArrayList<String> tables = new ArrayList<>();
        try (ResultSet tablesResult = metaData.getTables(null, null, null, new String[]{"TABLE"})) {
            while (tablesResult.next()) {
                String tableName = tablesResult.getString("TABLE_NAME"); // TODO extract String to enum o.ä.
                tables.add(tableName);
            }
        } catch (SQLException sqlException) {
            // TODO add logger
            System.out.println("SQL Exception");
        }
        return tables;
    }

    private ArrayList<Table> readTableColumns(ArrayList<String> tableNames) {
        ArrayList<Table> tables = new ArrayList<>();
        for (final String tableName : tableNames
        ) {
            try {
                ArrayList<Column> columns = new ArrayList<>();
                final ResultSet currentColumn = metaData.getColumns(null, null, tableName, null);
                while (currentColumn.next()) {
                    String columnName = currentColumn.getString("COLUMN_NAME");
                    columns.add(new Column(columnName));
                }
                tables.add(new Table(tableName, columns));
            } catch (SQLException e) {
                // TODO add logger
                System.out.println("SQL Exception");
                throw new RuntimeException(e);
            }
        }
        return tables;
    }

    public static void main(String[] args) throws SQLException {
        // Usage example, schema movies must exist!
        SourceConnectionHandler.getInstance().connectDatabase(new ConnectionDetails(DatabaseSystem.MYSQL, "localhost", 3306, "movies", "root", "example"));
        Connection conn = SourceConnectionHandler.getInstance().getConnection();

        SchemaReader schemaReader = new SchemaReader(conn);
        final ArrayList<Table> tables = schemaReader.readSchema().getTables();
        System.out.println(Arrays.toString(tables.toArray()));


        conn.close();
    }


}
