package de.hdm_stuttgart.mi.connect.model;

/*
Defines all currently supported database systems
 */
public enum DatabaseSystem {
    // TODO move to config file
    POSTGRES("pg_user","usename"),
    MYSQL("mysql.user","User"),
    MARIADB("mysql.user","User");

    public final String userTableName;
    public final String userNameColumnName;

    DatabaseSystem(String userTableName,String userNameColumnName) {
        this.userTableName = userTableName;
        this.userNameColumnName = userNameColumnName;
    }
}
