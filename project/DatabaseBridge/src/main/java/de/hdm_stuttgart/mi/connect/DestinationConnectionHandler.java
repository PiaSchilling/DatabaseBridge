package de.hdm_stuttgart.mi.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DestinationConnectionHandler implements ConnectionHandler {
    private static DestinationConnectionHandler INSTANCE;
    public Connection connection;

    private DestinationConnectionHandler() {
    }

    public static DestinationConnectionHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DestinationConnectionHandler();
        }

        return INSTANCE;
    }

    public boolean connectDatabase(ConnectionDetails connectionDetails) {
        try {
            this.connection = DriverManager.getConnection(connectionDetails.jdbcUri, connectionDetails.username, connectionDetails.password);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean connectionActive() {
        return this.connection != null;
    }
}
