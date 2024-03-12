package de.hdm_stuttgart.mi.connect.api;

import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

public interface ConnectionHandler {

    boolean connectionActive();

    Connection getConnection();

    DatabaseMetaData getDatabaseMetaData();

    void closeConnection();

    ConnectionDetails getConnectionDetails();
}
