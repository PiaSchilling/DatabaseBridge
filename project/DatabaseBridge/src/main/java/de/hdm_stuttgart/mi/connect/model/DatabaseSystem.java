package de.hdm_stuttgart.mi.connect.model;

/**
 * Defines all currently supported database systems and related constant property file names
 */
public enum DatabaseSystem {
    POSTGRES("postgres_consts.properties"),
    MYSQL("mySQL_consts.properties"),
    MARIADB("mariaDB_consts.properties");

    public final String propertyFileName;

    DatabaseSystem(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }
}
