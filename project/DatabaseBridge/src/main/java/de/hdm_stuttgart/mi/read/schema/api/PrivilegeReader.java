package de.hdm_stuttgart.mi.read.schema.api;

import de.hdm_stuttgart.mi.read.schema.model.ColumnPrivilege;
import de.hdm_stuttgart.mi.read.schema.model.Privilege;

import java.util.ArrayList;

public interface PrivilegeReader {
    /**
     * Read all table privileges from a schema
     *
     * @param schemaName the name of the schema the privileges should be read of
     * @return a list of privilege objects
     */
    ArrayList<Privilege> readTablePrivileges(String schemaName);

    /**
     * Read all column privileges from a schema
     * @param schemaName the name of the schema the privileges should be read of
     * @return a list of ColumnPrivilege objects
     * TODO this is currently not working for any database system
     */
    ArrayList<ColumnPrivilege> readColumnPrivileges(String schemaName);
}
