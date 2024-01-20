package de.hdm_stuttgart.mi.connect;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionHandler {
    boolean connectDatabase(ConnectionDetails connectionDetails) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, URISyntaxException, MalformedURLException, SQLException, NoSuchMethodException;
    boolean connectionActive();

    Connection getConnection();
}
