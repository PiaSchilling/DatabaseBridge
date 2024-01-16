package de.hdm_stuttgart.mi.connect;

public class ConnectionDetails {
    private final DatabaseSystem databaseSystem;
    private final String databaseDriverName;

    private final String databaseDriverJar;
    private final String hostAddress;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private String jdbcUri;

    /**
     * Class saves all parameters to connect to databases
     *
     * @param databaseSystem     the DatabaseSystem type
     * @param databaseDriverName name of the database driver (e. g. com.mysql.cj.jdbc.Driver)
     * @param databaseDriverJar  the location of the databaseDriver Jar (e.g. mysql-connector-j-8.0.33.jar)
     * @param hostAddress        host address of the database server (e.g. localhost)
     * @param port               port number, on which the database is accessible
     * @param database           In case only specific database should be copied
     * @param username           username to login into the database
     * @param password           passwort to login into the database
     */
    public ConnectionDetails(DatabaseSystem databaseSystem, String databaseDriverName, String databaseDriverJar, String hostAddress, int port, String database, String username, String password) {
        this.databaseSystem = databaseSystem;
        this.databaseDriverName = databaseDriverName;
        this.databaseDriverJar = databaseDriverJar;
        this.hostAddress = hostAddress;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.jdbcUri = this.createJdbcUri();
    }

    /**
    Creates a JDBC URI from given parameters
     @return  jdbc URI String needed to connect to database
     */
    private String createJdbcUri() {
        jdbcUri = "jdbc:";
        switch (this.databaseSystem) {
            case MYSQL:
                jdbcUri = jdbcUri + "mysql://" + this.hostAddress + ":" + this.port + "/" + this.database;
                break;
            case MARIADB:
                jdbcUri = jdbcUri + "mariadb://" + this.hostAddress + ":" + this.port + "/" + this.database;
                break;
            case POSTGRES:
                jdbcUri = jdbcUri + "postgresql://" + this.hostAddress + ":" + this.port + "/" + this.database;
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

    public String getDatabaseDriverName() {
        return databaseDriverName;
    }

    public String getDatabaseDriverJar() {
        return databaseDriverJar;
    }
}
