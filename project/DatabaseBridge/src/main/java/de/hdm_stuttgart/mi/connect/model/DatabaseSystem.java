package de.hdm_stuttgart.mi.connect.model;

/**
 * Defines all currently supported database systems and related constant property and types file names
 */
public enum DatabaseSystem {
    POSTGRES("postgres_consts.properties",
            "postgres_types.properties"),
    MYSQL("mySQL_consts.properties",
            "mySQL_types.properties"),
    MARIADB("mariaDB_consts.properties",
            "mariaDB_types.properties");

    public final String propertyFileName;
    public final String typesFileName;

    DatabaseSystem(String propertyFileName, String typesFileName) {
        this.propertyFileName = propertyFileName;
        this.typesFileName = typesFileName;
    }
}
