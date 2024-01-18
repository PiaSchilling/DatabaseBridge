package de.hdm_stuttgart.mi.read.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.read.api.PrivilegeReader;
import de.hdm_stuttgart.mi.read.api.SchemaReader;
import de.hdm_stuttgart.mi.read.api.TableReader;
import de.hdm_stuttgart.mi.read.api.UsersReader;
import de.hdm_stuttgart.mi.read.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchemaReaderImpl implements SchemaReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final DatabaseMetaData metaData;
    private final TableReader tableReader;
    private final UsersReader usersReader;

    private final PrivilegeReader privilegeReader;

    @Inject
    public SchemaReaderImpl(@Named("SourceDBMetaData") DatabaseMetaData databaseMetaData,
                            TableReader tableReader,
                            UsersReader usersReader,
                            PrivilegeReader privilegeReader) {
        this.tableReader = tableReader;
        this.metaData = databaseMetaData;
        this.usersReader = usersReader;
        this.privilegeReader = privilegeReader;
    }

    @Override
    public Schema readSchema(String schemaName) {
        final ArrayList<Table> tables = readTables(schemaName);
        final ArrayList<Table> views = readViews(schemaName);
        final ArrayList<User> users = usersReader.readUsers();
        final ArrayList<Privilege> tablePrivileges = privilegeReader.readTablePrivileges();
        final ArrayList<ColumnPrivilege> columnPrivileges = privilegeReader.readColumnPrivileges();

        return new Schema(tables, views, users, tablePrivileges, columnPrivileges);
    }

    private ArrayList<Table> readTables(String schemaName) {
        final ArrayList<String> tableNames = readTableNames(schemaName);
        final ArrayList<Table> tables = new ArrayList<>();

        for (String tableName : tableNames
        ) {
            tables.add(tableReader.readTable(tableName));
        }
        return tables;
    }

    private ArrayList<Table> readViews(String schemaName) {
        final ArrayList<String> viewNames = readViewNames(schemaName);
        final ArrayList<Table> views = new ArrayList<>();

        for (String viewName : viewNames
        ) {
            views.add(tableReader.readTable(viewName));
        }
        return views;
    }


    private ArrayList<String> readTableNames(String schemaName) {
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
