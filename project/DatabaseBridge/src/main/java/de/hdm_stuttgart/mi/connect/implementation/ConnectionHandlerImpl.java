package de.hdm_stuttgart.mi.connect.implementation;

import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.model.ConnectionType;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;

import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandlerImpl implements ConnectionHandler {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final ConnectionType connectionType;
    private Connection connection;

    private final ConnectionDetails connectionDetails;


    public ConnectionHandlerImpl(ConnectionType connectionType, ConnectionDetails connectionDetails) {
        this.connectionType = connectionType;
        this.connectionDetails = connectionDetails;
        if (connectionType == ConnectionType.SOURCE) {
            connectDatabase(connectionDetails); // TODO remove once destination DB should be used!
        }
    }

    private void connectDatabase(ConnectionDetails connectionDetails) {
        try {
            URL u = new URI(connectionDetails.getDatabaseDriverJar()).toURL();
            String classname = connectionDetails.getDatabaseDriverName();
            URLClassLoader ucl = new URLClassLoader(new URL[]{u});
            Driver driver = (Driver) Class.forName(classname, true, ucl).getDeclaredConstructor().newInstance();
            DriverManager.registerDriver(new DriverShim(driver));
            this.connection = DriverManager.getConnection(connectionDetails.getJdbcUri(), connectionDetails.getUsername(), connectionDetails.getPassword());
        } catch (MalformedURLException e) {
            System.out.println("Error: Couldn't find the jar file provided, please check if the path is correct");
            System.out.println(e.getMessage());
        } catch (URISyntaxException e) {
            System.out.println("Error: The path to the jar file is formatted incorrectly");
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Couldn't load the jdbc driver class");
            System.out.println(e.getMessage());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            System.out.println("Error: Couldn't create new instance of specified driver class");
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while trying to connect to Database: " + connectionDetails.getJdbcUri() + e.getMessage());
            System.out.println("Error: Couldn't connect to the database, please recheck connection details like username, password, host address, port, database name and database system");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks if a database connection is available
     * @return true if a connection is available, false otherwise
     */
    @Override
    public boolean connectionActive() {
        return this.connection != null;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error while closing connection: " + e.getMessage());
        }
    }

    @Override
    public DatabaseMetaData getDatabaseMetaData() {
        try {
            return this.connection.getMetaData();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while trying to extract metadata: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }
}
