package de.hdm_stuttgart.mi.connect.model;

/*
Class saves all parameters to connect to databases
 */
public class ConnectionDetails {
    private final DatabaseSystem databaseSystem;
    private final String databaseDriverName;
    private final String databaseDriverJar;
    private final String hostAddress;
    private final int port;
    private final String schema;
    private final String username;
    private final String password;
    private String jdbcUri;

    /**
     * Class saves all parameters to connect to databases
     *
     * @param databaseSystem     the DatabaseSystem type
     * @param databaseDriverName name of the database driver (e.g. com.mysql.cj.jdbc.Driver)
     * @param databaseDriverJar  the location of the databaseDriver Jar (e.g. mysql-connector-j-8.0.33.jar)
     * @param hostAddress        host address of the database server (e.g. localhost)
     * @param port               port number, on which the database is accessible
     * @param schema             the name of the schema which should be converted
     * @param username           username to login into the database
     * @param password           passwort to login into the database
     */
    public ConnectionDetails(DatabaseSystem databaseSystem, String databaseDriverName, String databaseDriverJar, String hostAddress, int port, String schema, String username, String password) {
        this.databaseSystem = databaseSystem;
        this.databaseDriverName = databaseDriverName;
        this.databaseDriverJar = databaseDriverJar;
        this.hostAddress = hostAddress;
        this.port = port;
        this.schema = schema;
        this.username = username;
        this.password = password;
        this.jdbcUri = this.createJdbcUri();
    }

    /**
     * Creates a JDBC URI from given parameters
     * Note: since database systems differ in behavior, for some the schema-name might not be added to the jdbc uri since
     * it would cause problems. The schema-name is specified on schema-read again, to keep the desired functionality
     *
     * @return the JDBC URI to connect to the database
     */
    private String createJdbcUri() {
        jdbcUri = "jdbc:";
        switch (this.databaseSystem) {
            case MYSQL -> jdbcUri = jdbcUri + "mysql://" + this.hostAddress + ":" + this.port + "/" + schema;
            case MARIADB -> jdbcUri = jdbcUri + "mariadb://" + this.hostAddress + ":" + this.port + "/" + schema;
            case POSTGRES -> jdbcUri = jdbcUri + "postgresql://" + this.hostAddress + ":" + this.port + "/";
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

    public String getDatabaseDriverName() {
        return databaseDriverName;
    }

    public String getDatabaseDriverJar() {
        return "jar:file:" + databaseDriverJar + "!/";
    }
}
