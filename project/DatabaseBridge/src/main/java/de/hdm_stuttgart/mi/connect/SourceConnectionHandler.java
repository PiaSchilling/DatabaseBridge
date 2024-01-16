package de.hdm_stuttgart.mi.connect;

import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.sql.Connection;
import java.sql.Driver;
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
    public boolean connectDatabase(ConnectionDetails connectionDetails) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, URISyntaxException, SQLException, NoSuchMethodException {
        URL u;
        try {
            u = new URI(connectionDetails.getDatabaseDriverJar()).toURL();
        }
       catch(MalformedURLException e) {
           System.out.println("Error: Couldn't find the file provided by the URL");
           System.out.println(e.getMessage());
           return false;
       }
        String classname = connectionDetails.getDatabaseDriverName();
        URLClassLoader ucl = new URLClassLoader(new URL[] { u });
        Driver d = (Driver)Class.forName(classname, true, ucl).getDeclaredConstructor().newInstance();
        DriverManager.registerDriver(new DriverShim(d));
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
