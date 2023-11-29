package de.hdm_stuttgart.mi.connect;

public class ConnectionDetails {
    final DatabaseSystem databaseSystem;
    final String hostAddress;
    final int port;
    final String schema;
    final String username;
    final String password;
    String jdbcUri;

    public ConnectionDetails(DatabaseSystem databaseSystem, String hostAddress, int port, String schema, String username, String password) {
        this.databaseSystem = databaseSystem;
        this.hostAddress = hostAddress;
        this.port = port;
        this.schema = schema;
        this.username = username;
        this.password = password;
        this.jdbcUri = this.CreateJdbcUri();
    }

    public ConnectionDetails get() {
        return this;
    }

    private String CreateJdbcUri() {
        String jdbcUri = "jdbc:";
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
}
