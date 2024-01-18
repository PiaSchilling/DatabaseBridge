package de.hdm_stuttgart.mi.read.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.read.api.SchemaReader;
import de.hdm_stuttgart.mi.read.api.TableReader;
import de.hdm_stuttgart.mi.read.model.Schema;
import de.hdm_stuttgart.mi.read.model.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchemaReaderImpl implements SchemaReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final DatabaseMetaData metaData;
    private final TableReader tableReader;

    @Inject
    public SchemaReaderImpl(@Named("SourceDBMetaData") DatabaseMetaData databaseMetaData, TableReader tableReader) {
        this.tableReader = tableReader;
        this.metaData = databaseMetaData;
    }

    @Override
    public Schema readSchema(String schemaName) {
        final ArrayList<String> tableNames = readTableNames(schemaName);
        final ArrayList<String> viewNames = readViewNames(schemaName);

        ArrayList<Table> tables = new ArrayList<>();
        ArrayList<Table> views = new ArrayList<>();

        for (String tableName : tableNames
        ) {
            tables.add(tableReader.readTable(tableName));
        }

        for (String viewName : viewNames
        ) {
            views.add(tableReader.readTable(viewName));
        }

        return new Schema(tables,views);
    }


    private ArrayList<String> readTableNames(String schemaName) { // TODO hier weitermachen: rausfinden, wie man die user defined views von system views trennt: evtl. üer getCatalogs
        ArrayList<String> tables = new ArrayList<>();
        try (ResultSet tablesResult = metaData.getTables(schemaName, schemaName, null, new String[]{"TABLE"})) {
            while (tablesResult.next()) {
                String tableName = tablesResult.getString("TABLE_NAME");
                tables.add(tableName);
            }
        } catch (SQLException sqlException) {
            log.log(Level.SEVERE, "SQLException while reading table names: " + sqlException.getMessage());
        }
        return tables;
    }

    private ArrayList<String> readViewNames(String schemaName) {
        // TODO read view definition (which select statement was used to create the view?)
        ArrayList<String> views = new ArrayList<>();
        try (ResultSet tablesResult = metaData.getTables(schemaName, schemaName, null, new String[]{"VIEW"})) {
            System.out.println(metaData.getUserName());

            while (tablesResult.next()) {
                String tableName = tablesResult.getString("TABLE_NAME");
                views.add(tableName);
            }
        } catch (SQLException sqlException) {
            log.log(Level.SEVERE, "SQLException while reading view names: " + sqlException.getMessage());
        }
        System.out.println(Arrays.toString(views.toArray()));
        return views;
    }
}
