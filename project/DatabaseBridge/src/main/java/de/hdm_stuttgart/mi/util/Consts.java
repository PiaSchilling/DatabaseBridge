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
     * @param arguments params to fill the placeholders,2 arguments required: viewName,schemaName
     * @return the valid SQL query containing the arguments
     */
    public static String viewStmtQuery(Object... arguments) {
        return propLoader.getPlaceholderProperty("viewStmtQuery", arguments);
    }

    /**
     * SQL statement to drop a schema
     *
     * @param arguments params to fill the placeholders, 1 argument required: schemaName
     * @return the valid SQL statement containing the arguments
     */
    public static String dropSchemaStmt(Object... arguments) {
        return propLoader.getPlaceholderProperty("dropSchemaStmt", arguments);
    }

    /**
     * The name of the table which holds all users
     */
    final public static String userTableName = propLoader.getProperty("userTableName");
    /**
     * The name of the column in the users table that contains usernames
     */
    final public static String userNameColName = propLoader.getProperty("userNameColName");

    /**
     * The name of the auto increment constraint
     */
    final public static String autoIncrementConstraintName = propLoader.getProperty("autoIncrementConstraintName");

    /**
     * The maximum length of a varchar
     */
    final public static int varcharMaxLength = Integer.parseInt(propLoader.getProperty("varcharMaxLength"));


}
