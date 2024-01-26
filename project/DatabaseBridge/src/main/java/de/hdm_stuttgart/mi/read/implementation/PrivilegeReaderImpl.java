package de.hdm_stuttgart.mi.read.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.read.model.AccessType;
import de.hdm_stuttgart.mi.read.model.ColumnPrivilege;
import de.hdm_stuttgart.mi.read.model.Privilege;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrivilegeReaderImpl implements de.hdm_stuttgart.mi.read.api.PrivilegeReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final DatabaseMetaData metaData;

    @Inject
    public PrivilegeReaderImpl(@Named("SourceDBMetaData") DatabaseMetaData databaseMetaData) {
        this.metaData = databaseMetaData;
    }

    @Override
    public ArrayList<Privilege> readTablePrivileges(String schemaName) {
        ArrayList<Privilege> tablePrivileges = new ArrayList<>();
        try (ResultSet privilegesResult = metaData.getTablePrivileges(null, schemaName, null)) {
            while (privilegesResult.next()) {
                final String tableName = privilegesResult.getString("TABLE_NAME");
                final String grantor = privilegesResult.getString("GRANTOR");
                final String grantee = privilegesResult.getString("GRANTEE");
                final String accessType = privilegesResult.getString("PRIVILEGE");

                tablePrivileges.add(new Privilege(
                        tableName,
                        grantor,
                        grantee,
                        AccessType.fromString(accessType)));
            }
        } catch (SQLException sqlException) {
            log.log(Level.SEVERE, "SQLException while reading tablePrivileges: " + sqlException.getMessage());
        }
        return tablePrivileges;
    }

    @Override
    public ArrayList<ColumnPrivilege> readColumnPrivileges(String schemaName) {
        // TODO does not work for mysql and mariaDB
        ArrayList<ColumnPrivilege> columnPrivileges = new ArrayList<>();
        try (ResultSet privilegesResult = metaData.getColumnPrivileges(null, schemaName, "employees", null)) {
            while (privilegesResult.next()) {
                final String tableName = privilegesResult.getString("TABLE_NAME");
                final String columnName = privilegesResult.getString("COLUMN_NAME");
                final String grantor = privilegesResult.getString("GRANTOR");
                final String grantee = privilegesResult.getString("GRANTEE");
                final String accessType = privilegesResult.getString("PRIVILEGE");

                columnPrivileges.add(new ColumnPrivilege(
                        tableName,
                        grantor,
                        grantee,
                        AccessType.fromString(accessType),
                        columnName));
            }
        } catch (SQLException sqlException) {
            log.log(Level.SEVERE, "SQLException while reading columnPrivileges: " + sqlException.getMessage());
        }
        return columnPrivileges;
    }
}
