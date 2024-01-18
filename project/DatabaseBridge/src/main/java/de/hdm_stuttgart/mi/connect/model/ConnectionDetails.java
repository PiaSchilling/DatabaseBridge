package de.hdm_stuttgart.mi.connect.model;

/*
Class saves all parameters to connect to databases
 */
public class ConnectionDetails {
    private final DatabaseSystem databaseSystem;
    private final String hostAddress;
    private final int port;
    private final String schema;
    private final String username;
    private final String password;
    private String jdbcUri;

    /**
     * Model class to encapsulate connection details
     * @param databaseSystem the type of the databaseSystem
     * @param hostAddress host address of the database
     * @param port the port on which the database is available
     * @param schema the name of the schema which should be converted
     * @param username the name of the user who started the conversion
     * @param password the password of the user who started the conversion
     */
    public ConnectionDetails(DatabaseSystem databaseSystem, String hostAddress, int port, String schema, String username, String password) {
        this.databaseSystem = databaseSystem;
        this.hostAddress = hostAddress;
        this.port = port;
        this.schema = schema;
        this.username = username;
        this.password = password;
        this.jdbcUri = this.createJdbcUri();
    }

    /**
     *  Creates a JDBC URI from given parameters.
     *  Note: schema names are not added to the end of the jdbc uri since it causes problems for some database systems,
     *  so the schema is only specified when the schema is read
     * @return the JDBC URI to connect to the database
     */
    private String createJdbcUri() {
        jdbcUri = "jdbc:";
        switch (this.databaseSystem) {
            case MYSQL -> jdbcUri = jdbcUri + "mysql://" + this.hostAddress + ":" + this.port + "/" ;
            case MARIADB -> jdbcUri = jdbcUri + "mariadb://" + this.hostAddress + ":" + this.port + "/";
            case POSTGRES ->
                    jdbcUri = jdbcUri + "postgresql://" + this.hostAddress + ":" + this.port + "/";
        }

        return jdbcUri;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getJdbcUri() {
        return jdbcUri;
    }

    public String getSchema() {
        return schema;
    }

    public DatabaseSystem getDatabaseSystem() {
        return databaseSystem;
    }
}
