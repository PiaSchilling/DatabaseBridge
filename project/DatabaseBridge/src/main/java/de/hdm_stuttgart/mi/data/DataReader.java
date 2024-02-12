package de.hdm_stuttgart.mi.data;

import de.hdm_stuttgart.mi.read.model.Schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataReader {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    final private Schema schema;
    final private Connection connection;

    /**
     * ReadData reads all data records from one schema
     *
     * @param schema the schema the data should be read from
     * @param connection the database connection
     */
    public DataReader(Schema schema, Connection connection) {
        this.schema = schema;
        this.connection = connection;
    }

    /**
     * Read all data from tables in schema
     * @return tables, arrayList of ResultSet containing all table records
     */
    public ArrayList<ResultSet> readTableData() {
        ArrayList<ResultSet> tables = new ArrayList<>();
        //Get all tables in schema
        try{
            for(int i = 0; i < schema.tables().size(); i++) {
                //TODO: Close statement after accessing the resultSet
                final Statement stmt = connection.createStatement();
                tables.add(stmt.executeQuery("SELECT * FROM " + schema.name() + '.' + schema.tables().get(i).name()));
            }
        } catch (SQLException sqlException) {
            log.log(Level.SEVERE, "SQLException while reading table data: " + sqlException.getMessage());
        }
        return tables;
    }
}
