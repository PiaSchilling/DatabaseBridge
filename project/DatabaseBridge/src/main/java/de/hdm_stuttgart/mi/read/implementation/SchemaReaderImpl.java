package de.hdm_stuttgart.mi.read.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.read.api.*;
import de.hdm_stuttgart.mi.read.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchemaReaderImpl implements SchemaReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final DatabaseMetaData metaData;
    private final TableReader tableReader;
    private final ViewReader viewReader;
    private final UsersReader usersReader;
    private final PrivilegeReader privilegeReader;

    @Inject
    public SchemaReaderImpl(@Named("SourceDBMetaData") DatabaseMetaData databaseMetaData,
                            TableReader tableReader,
                            ViewReader viewReader,
                            UsersReader usersReader,
                            PrivilegeReader privilegeReader) {
        this.tableReader = tableReader;
        this.viewReader = viewReader;
        this.metaData = databaseMetaData;
        this.usersReader = usersReader;
        this.privilegeReader = privilegeReader;
    }

    @Override
    public Schema readSchema(String schemaName) {
        final ArrayList<Table> tables = readTables(schemaName);
        final ArrayList<View> views = viewReader.readViews(schemaName);
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
}
