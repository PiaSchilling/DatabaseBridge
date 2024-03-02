package de.hdm_stuttgart.mi.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatementExecutor {

    private static final Logger log = Logger.getLogger(StatementExecutor.class.getName());

    public static void executeWrite(Connection connection, String statement) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(statement);
            log.log(Level.FINE, "Successfully executed the statement: " + statement);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Exception while trying to execute the statement: " + statement + ", " + e.getMessage());
        }
    }
}
