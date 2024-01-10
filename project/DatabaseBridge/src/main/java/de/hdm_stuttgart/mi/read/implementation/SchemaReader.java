package de.hdm_stuttgart.mi.read.implementation;

import de.hdm_stuttgart.mi.connect.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.DatabaseSystem;
import de.hdm_stuttgart.mi.connect.SourceConnectionHandler;
import de.hdm_stuttgart.mi.read.model.Schema;
import de.hdm_stuttgart.mi.read.model.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchemaReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final DatabaseMetaData metaData;
    private final TableReader tableReader;


    public SchemaReader(Connection connection, TableReader tableReader) {
        try {
            this.metaData = connection.getMetaData();
            this.tableReader = tableReader;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Not able to extract metadata from connection");
            // TODO add error hanlding
            throw new RuntimeException(e);
        }
    }

    public Schema readSchema() {
        final ArrayList<String> tableNames = readTableNames();
        ArrayList<Table> tables = new ArrayList<>();

        for (String tableName : tableNames
        ) {
            tables.add(tableReader.readTable(tableName));
        }

        return new Schema(tables);
    }


    private ArrayList<String> readTableNames() {
        ArrayList<String> tables = new ArrayList<>();
        try (ResultSet tablesResult = metaData.getTables(null, null, null, new String[]{"TABLE"})) {
            while (tablesResult.next()) {
                String tableName = tablesResult.getString("TABLE_NAME");
                tables.add(tableName);
            }
        } catch (SQLException sqlException) {
            log.log(Level.SEVERE, "SQLException while reading table names: " + sqlException.getMessage());
        }
        return tables;
    }

    private ArrayList<String> readViewNames() {
        ArrayList<String> views = new ArrayList<>();
        // TODO implement me
        return views;
    }


    public static void main(String[] args) throws SQLException {
        // Usage example, schema movies must exist!
        SourceConnectionHandler.getInstance().connectDatabase(new ConnectionDetails(DatabaseSystem.MYSQL, "localhost", 3306, "movies", "root", "example"));
        Connection conn = SourceConnectionHandler.getInstance().getConnection();

       // SchemaReader schemaReader = new SchemaReader(conn);
        //final ArrayList<Table> tables = schemaReader.readSchema().getTables();
        //System.out.println(Arrays.toString(tables.toArray()));


        conn.close();
    }


}
