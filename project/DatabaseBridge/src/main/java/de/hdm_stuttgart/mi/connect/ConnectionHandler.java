package de.hdm_stuttgart.mi.connect;

import java.sql.Connection;

public interface ConnectionHandler {
    boolean connectDatabase(ConnectionDetails connectionDetails);
    boolean connectionActive();

    Connection getConnection();
}
