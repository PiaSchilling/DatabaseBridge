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

    public ConnectionDetails(DatabaseSystem databaseSystem, String hostAddress, int port, String schema, String username, String password) {
        this.databaseSystem = databaseSystem;
        this.hostAddress = hostAddress;
        this.port = port;
        this.schema = schema;
        this.username = username;
        this.password = password;
        this.jdbcUri = this.createJdbcUri();
    }

    /*
    Creates a JDBC URI from given parameters
     */
    private String createJdbcUri() {
        jdbcUri = "jdbc:";
        switch (this.databaseSystem) {
            case MYSQL:
                jdbcUri = jdbcUri + "mysql://" + this.hostAddress + ":" + this.port + "/" + this.schema;
                break;
            case MARIADB:
                jdbcUri = jdbcUri + "mariadb://" + this.hostAddress + ":" + this.port + "/" + this.schema;
                break;
            case POSTGRES:
                jdbcUri = jdbcUri + "postgresql://" + this.hostAddress + ":" + this.port + "/" + this.schema;
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
}
