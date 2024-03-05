package de.hdm_stuttgart.mi.read.schema.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.read.schema.api.PrivilegeReader;
import de.hdm_stuttgart.mi.read.schema.model.AccessType;
import de.hdm_stuttgart.mi.read.schema.model.Privilege;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrivilegeReaderImpl implements PrivilegeReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final DatabaseMetaData metaData;

    @Inject
    public PrivilegeReaderImpl(@Named("SourceDBMetaData") DatabaseMetaData databaseMetaData) {
        this.metaData = databaseMetaData;
    }

    @Override
    public ArrayList<Privilege> readTablePrivileges(String schemaName) {
        ArrayList<Privilege> tablePrivileges = new ArrayList<>();
        try (ResultSet privilegesResult = metaData.getTablePrivileges(schemaName, schemaName, null)) {
            while (privilegesResult.next()) {
                final String tableName = privilegesResult.getString("TABLE_NAME");
                final String grantor = privilegesResult.getString("GRANTOR");
                final String grantee = privilegesResult.getString("GRANTEE");
                final String accessType = privilegesResult.getString("PRIVILEGE");

                // Do not add privileges for system users like mysql.sys, postgres, ...
                // Explicitly grant privileges to current logged-in user also if it's named like a system user
                if (!SourceConsts.systemUserNames.contains(grantee) || metaData.getUserName().equals(grantee)) {
                    tablePrivileges.add(new Privilege(
                            tableName,
                            grantor,
                            grantee,
                            AccessType.fromString(accessType)));
                }
            }
        } catch (SQLException sqlException) {
            log.log(Level.SEVERE, "SQLException while reading tablePrivileges: " + sqlException.getMessage());
        }
        return tablePrivileges;
    }
}
