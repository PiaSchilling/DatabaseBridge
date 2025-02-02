package de.hdm_stuttgart.mi.connect.implementation;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class DriverShim implements Driver {
    private final Driver driver;

    /**
     *  This class makes it possible to pick the JDBC Driver at runtime
     * @param driver JDBC driver class that will be used by the application
     */
    public DriverShim(Driver driver) {
        this.driver = driver;
    }
    @Override
    public boolean acceptsURL(String u) throws SQLException {
        return this.driver.acceptsURL(u);
    }
    @Override
    public Connection connect(String url, Properties properties) throws SQLException {
        return this.driver.connect(url, properties);
    }

    @Override
    public int getMajorVersion() {
        return this.driver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return this.driver.getMinorVersion();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
        return this.driver.getPropertyInfo(u, p);
    }

    @Override
    public boolean jdbcCompliant() {
        return this.driver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }
}
