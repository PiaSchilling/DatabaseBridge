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
    public boolean connectDatabase(ConnectionDetails connectionDetails) {
        try {
            URL u;
            u = new URI(connectionDetails.getDatabaseDriverJar()).toURL();
            String classname = connectionDetails.getDatabaseDriverName();
            URLClassLoader ucl = new URLClassLoader(new URL[] { u });
            Driver driver = (Driver)Class.forName(classname, true, ucl).getDeclaredConstructor().newInstance();
            DriverManager.registerDriver(new DriverShim(driver));
            this.connection = DriverManager.getConnection(connectionDetails.getJdbcUri(), connectionDetails.getUsername(), connectionDetails.getPassword());
            return true;
        }
       catch(MalformedURLException e ) {
           System.out.println("Error: Couldn't find the jar file provided, please check if the path is correct");
           System.out.println(e.getMessage());
           return false;
       }
        catch(URISyntaxException e) {
            System.out.println("Error: The path to the jar file is formatted incorrectly");
            System.out.println(e.getMessage());
            return false;
        }
        catch(ClassNotFoundException e) {
            System.out.println("Error: Couldn't load the jdbc driver class");
            System.out.println(e.getMessage());
            return false;
        }
        catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Error: Couldn't create new instance of specified driver class");
            System.out.println(e.getMessage());
            return false;
        }
        catch (SQLException e) {
            System.out.println("Error: Couldn't connect to the database, please recheck connection details like username, password, host address, port, database name and database system");
            System.out.println(e.getMessage());
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
