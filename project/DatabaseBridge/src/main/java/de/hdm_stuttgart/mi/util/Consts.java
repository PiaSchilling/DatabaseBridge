package de.hdm_stuttgart.mi.util;

public class Consts {
    private static final DbSysConstsLoader propLoader = DbSysConstsLoader.INSTANCE();

    // Postgres (PG) related consts - - - - - - -
    // Views
    /**
     * The name of the column of a viewDefinition table which contains the createView SQl statement in a postgres database
     */
    final public static String viewStmtColNamePG = propLoader.getProperty("viewStmtColNamePG");

    /**
     * SQL query to select createViewStatements in a postgres database with placeholders
     *
     * @param arguments params to fill the placeholders
     */
    public static String viewStmtQueryPG(Object... arguments) {
        return propLoader.getPlaceholderProperty("viewStmtQueryPG", arguments);
    }

    /**
     * The name of the table in a postgres database which holds all users
     */
    final public static String userTableNamePG = propLoader.getProperty("userTableNamePG");
    /**
     * The name of the column in the users table that contains usernames in a postgres database
     */
    final public static String userNameColNamePG = propLoader.getProperty("userNameColNamePG");

    // MySQL (MS) and MariaDB (MD) related consts - - - - - - -
    // Views
    /**
     * SQL query to select createViewStatements in a mariadb or mysql database
     */
    final public static String viewStmtQueryMSMD = propLoader.getProperty("viewStmtQueryMSMD");
    /**
     * The name of the column of a viewDefinition table which contains the createView SQl statement in a mariadb or mysql database
     */
    final public static String viewStmtColNameMSMD = propLoader.getProperty("viewStmtColNameMSMD");

    //Users
    /**
     * The name of the table in a mysql or mariadb database which holds all users
     */
    final public static String userTableNameMSMD = propLoader.getProperty("userTableNameMSMD");
    /**
     * The name of the column in the users table that contains usernames in a mysql or mariadb database
     */
    final public static String userNameColNameMSMD = propLoader.getProperty("userNameColNameMSMD");
}
