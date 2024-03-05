package de.hdm_stuttgart.mi.util.consts;


/**
 * Class holds database system specific constant values of the source database.
 * The database system has to be defined in the DbSysConstsLoaders init-method before using this class.
 */
public class SourceConsts {
    private static final DbSysConstsLoader propLoader = SourceDbSysConstsLoader.INSTANCE();

    /**
     * The name of the column of a viewDefinition table which contains the createView SQl statement
     */
    final public static String viewStmtColName = propLoader.getConstant("viewStmtColName");

    /**
     * SQL query to select createViewStatements with placeholders
     *
     * @param arguments params to fill the placeholders,2 arguments required: viewName,schemaName
     * @return the valid SQL query containing the arguments
     */
    public static String viewStmtQuery(Object... arguments) {
        return propLoader.getPlaceholderConst("viewStmtQuery", arguments);
    }

    /**
     * The name of the table which holds all users
     */
    final public static String userTableName = propLoader.getConstant("userTableName");
    /**
     * The name of the column in the users table that contains usernames
     */
    final public static String userNameColName = propLoader.getConstant("userNameColName");


    /**
     * The maximum length of a varchar
     */
    final public static int varcharMaxLength = Integer.parseInt(propLoader.getConstant("varcharMaxLength"));

    final public static String viewRegex = propLoader.getConstant("viewRegex");

    final public static String viewReplacement = propLoader.getConstant("viewReplacement");


}
