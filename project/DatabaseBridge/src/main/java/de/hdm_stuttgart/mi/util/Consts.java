package de.hdm_stuttgart.mi.util;


/**
 * Class holds database system specific constant values.
 * The database system has to be defined in the DbSysConstsLoaders init-method before using this class.
 */
public class Consts {
    private static final DbSysConstsLoader propLoader = DbSysConstsLoader.INSTANCE();

    /**
     * The name of the column of a viewDefinition table which contains the createView SQl statement
     */
    final public static String viewStmtColName = propLoader.getProperty("viewStmtColName");

    /**
     * SQL query to select createViewStatements with placeholders
     *
     * @param arguments params to fill the placeholders
     */
    public static String viewStmtQuery(Object... arguments) {
        return propLoader.getPlaceholderProperty("viewStmtQuery", arguments);
    }

    /**
     * The name of the table which holds all users
     */
    final public static String userTableName = propLoader.getProperty("userTableName");
    /**
     * The name of the column in the users table that contains usernames
     */
    final public static String userNameColName = propLoader.getProperty("userNameColName");


}
