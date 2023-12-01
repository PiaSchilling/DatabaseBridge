package de.hdm_stuttgart.mi.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
Instance used to connect to the source database
 */
public class SourceConnectionHandler implements ConnectionHandler {
    private static SourceConnectionHandler INSTANCE;
    private Connection connection;

    private SourceConnectionHandler() {
    }

    public static SourceConnectionHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SourceConnectionHandler();
        }
        return INSTANCE;
    }

    /*
   Connects parameter connection to a specific database
   return true if connection is possible
   return false if connection is not possible
    */
    public boolean connectDatabase(ConnectionDetails connectionDetails) {
        try {
            this.connection = DriverManager.getConnection(connectionDetails.getJdbcUri(), connectionDetails.getUsername(), connectionDetails.getPassword());
            return true;
        } catch (SQLException var3) {
            return false;
        }
    }

    /*
   Checks if a database connection is available
    */
    public boolean connectionActive() {
        return this.connection != null;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
