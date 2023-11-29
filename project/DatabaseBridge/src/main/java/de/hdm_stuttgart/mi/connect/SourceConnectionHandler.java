package de.hdm_stuttgart.mi.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SourceConnectionHandler implements ConnectionHandler {
    private static SourceConnectionHandler INSTANCE;
    public Connection connection;

    private SourceConnectionHandler() {
    }

    public static SourceConnectionHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SourceConnectionHandler();
        }

        return INSTANCE;
    }

    public boolean connectDatabase(ConnectionDetails connectionDetails) {
        try {
            this.connection = DriverManager.getConnection(connectionDetails.jdbcUri, connectionDetails.username, connectionDetails.password);
            return true;
        } catch (SQLException var3) {
            return false;
        }
    }

    public boolean connectionActive() {
        return this.connection != null;
    }
}
