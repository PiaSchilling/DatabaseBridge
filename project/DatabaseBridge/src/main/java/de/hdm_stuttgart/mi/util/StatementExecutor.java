package de.hdm_stuttgart.mi.util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatementExecutor {

    private static final Logger log = Logger.getLogger(StatementExecutor.class.getName());

    /**
     * Execute a sql statement which updates/writes to the DB
     *
     * @param connection the connection to the DB to which should be written
     * @param statement  the statement which should be executed
     */
    public static void executeWrite(Connection connection, String statement) {
        if (statement.isBlank()) {
            return;
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(statement);
            log.log(Level.FINE, "Successfully executed the statement: " + statement);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Exception while trying to execute the statement: " + statement + ", " + e.getMessage());
        }
    }
}
