package de.hdm_stuttgart.mi.util.consts;


import de.hdm_stuttgart.mi.util.SQLType;

/**
 * Class holds database system specific constant values of the destination database.
 * The database system has to be defined in the DbSysConstsLoaders init-method before using this class.
 */
public class DestinationConsts {
    private static final DbSysConstsLoader propLoader = DestinationDbSysConstsLoader.INSTANCE();

    /**
     * SQL statement to drop a schema
     *
     * @param arguments params to fill the placeholders, 1 argument required: schemaName
     * @return the valid SQL statement containing the arguments
     */
    public static String dropSchemaStmt(Object... arguments) {
        return propLoader.getPlaceholderConst("dropSchemaStmt", arguments);
    }

    public static String getType(SQLType type){
        return propLoader.getType(type);
    }

    /**
     * The name of the auto increment constraint
     */
    final public static String autoIncrementConstraintName = propLoader.getConstant("autoIncrementConstraintName");


    final public static String tempPassword = propLoader.getConstant("tempPassword");

    /**
     * SQL statement to create a user
     *
     * @param arguments params to fill the placeholders, 2 arguments required: userName and userPassword
     * @return the valid SQL statement containing the arguments
     */
    public static String createUserStmt(Object... arguments) {
        return propLoader.getPlaceholderConst("createUserStmt", arguments);
    }

}
