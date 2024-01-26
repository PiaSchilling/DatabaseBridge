package de.hdm_stuttgart.mi.connect.model;

import de.hdm_stuttgart.mi.util.Consts;

/**
 * Defines all currently supported database systems and related constant values
 */
public enum DatabaseSystem {

    POSTGRES(Consts.userTableNamePG,
            Consts.userNameColNamePG,
            Consts.viewStmtColNamePG,
            ""), // requires placeholders
    MYSQL(Consts.userTableNameMSMD,
            Consts.userNameColNameMSMD,
            Consts.viewStmtColNameMSMD,
            Consts.viewStmtQueryMSMD),
    MARIADB(Consts.userTableNameMSMD,
            Consts.userNameColNameMSMD,
            Consts.viewStmtColNameMSMD,
            Consts.viewStmtQueryMSMD);

    public final String userTableName;
    public final String userNameColumnName;

    public final String viewColumnName;

    public final String viewQuery;

    DatabaseSystem(String userTableName, String userNameColumnName, String viewColumnName, String viewQuery) {
        this.userTableName = userTableName;
        this.userNameColumnName = userNameColumnName;
        this.viewColumnName = viewColumnName;
        this.viewQuery = viewQuery;
    }
}
