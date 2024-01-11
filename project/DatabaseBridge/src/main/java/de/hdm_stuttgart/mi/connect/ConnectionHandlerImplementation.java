package de.hdm_stuttgart.mi.connect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandlerImplementation implements ConnectionHandler {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final ConnectionType connectionType;
    private Connection connection;


    public ConnectionHandlerImplementation(ConnectionType connectionType, ConnectionDetails connectionDetails) {
        this.connectionType = connectionType;
        if (connectionType == ConnectionType.SOURCE) {
            connectDatabase(connectionDetails); // TODO remove once destination DB should be used!
        }
    }

    private void connectDatabase(ConnectionDetails connectionDetails) {
        try {
            this.connection = DriverManager.getConnection(
                    connectionDetails.getJdbcUri(),
                    connectionDetails.getUsername(),
                    connectionDetails.getPassword());
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while trying to connect to Database: " +
                    connectionDetails.getJdbcUri() + e.getMessage());
        }
    }

    @Override
    public boolean connectionActive() {
        return this.connection != null;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error while closing connection: " + e.getMessage());
        }
    }

    @Override
    public DatabaseMetaData getDatabaseMetaData() {
        try {
            return this.connection.getMetaData();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while trying to extract metadata: " + e.getMessage());
            return null;
        }
    }
}
