package de.hdm_stuttgart.mi.read.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.read.api.SchemaReader;
import de.hdm_stuttgart.mi.read.api.TableReader;
import de.hdm_stuttgart.mi.read.model.Schema;
import de.hdm_stuttgart.mi.read.model.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchemaReaderImplementation implements SchemaReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final DatabaseMetaData metaData;
    private final TableReader tableReader;

    @Inject
    public SchemaReaderImplementation(@Named("SourceDBMetaData") DatabaseMetaData databaseMetaData, TableReader tableReader) {
        this.tableReader = tableReader;
        this.metaData = databaseMetaData;
    }

    @Override
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
}
