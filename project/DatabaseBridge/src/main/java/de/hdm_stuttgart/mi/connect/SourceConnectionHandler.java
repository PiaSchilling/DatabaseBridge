package de.hdm_stuttgart.mi.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SourceConnectionHandler implements ConnectionHandler {
    private static SourceConnectionHandler INSTANCE;
    private Connection connection;

    /**
     Instance to connect to the source database
     */
    private SourceConnectionHandler() {
    }

    /**
     *
     * @return Instance of SourceConnectionHandler
     */
    public static SourceConnectionHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SourceConnectionHandler();
        }
        return INSTANCE;
    }

    /**
     * Connects parameter connection to a specific database
     * @param connectionDetails configuration parameters for the connection
     * @return return true if connection is possible, return false if connection is not possible
     */
    public boolean connectDatabase(ConnectionDetails connectionDetails) {
        try {
            this.connection = DriverManager.getConnection(connectionDetails.getJdbcUri(), connectionDetails.getUsername(), connectionDetails.getPassword());
            return true;
        } catch (SQLException var3) {
            return false;
        }
    }

    /**
     * Checks if a database connection is available
     * @return true if a connection is available, false otherwise
     */
    public boolean connectionActive() {
        return this.connection != null;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
