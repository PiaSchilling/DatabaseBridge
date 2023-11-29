package de.hdm_stuttgart.mi.connect;

public interface ConnectionHandler {
    boolean connectDatabase(ConnectionDetails connectionDetails);
    boolean connectionActive();
}
