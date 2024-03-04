package de.hdm_stuttgart.mi.write.schema.implementation;

import de.hdm_stuttgart.mi.read.schema.model.AccessType;
import de.hdm_stuttgart.mi.read.schema.model.Privilege;
import de.hdm_stuttgart.mi.read.schema.model.User;
import de.hdm_stuttgart.mi.util.consts.DestinationConsts;

public class UserStatementBuilder {

    /**
     * Get the CREATE USER statement for the provided user
     *
     * @param user the user for which the sql statement should be built
     * @return a sql statement string which can be used to create the provided user
     */
    public static String createUserStatement(final User user) {
        return DestinationConsts.createUserStmt(user.username(), DestinationConsts.tempPassword) + "\n";
    }

    /**
     * Get the GRANT privilege [SELECT,DELETE,...] statement for the provided privilege
     *
     * @param privilege the privilege for which the sql statement should be built
     * @return a sql statement string which can be used to grant a privilege on a table to a user
     */
    public static String grantPrivilegeStatement(final Privilege privilege, final String schemaName) {
        final String accessTypeString = privilege.getAccessType() == AccessType.TRUNCATE
                ? DestinationConsts.accessType_TRUNCATE
                : privilege.getAccessType().toString();

        return "GRANT " + accessTypeString + " ON " + schemaName + "." + privilege.getTableName() + " TO "
                + privilege.getGrantee() + ";";
    }
}
