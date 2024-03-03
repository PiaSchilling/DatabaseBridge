package de.hdm_stuttgart.mi.util.consts;

import de.hdm_stuttgart.mi.connect.model.DatabaseSystem;
import de.hdm_stuttgart.mi.util.SQLType;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads database system specific constants like select queries or table column names
 */
public abstract class DbSysConstsLoader {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    // DB specific constant properties (like system table names, ...)
    private Properties constsProps;

    // DB specific types properties
    private Properties typesProps;

    private boolean isInitialized = false;


    /**
     * Initialize the provider by loading the according .properties into databaseSystemsConsts
     *
     * @param databaseSystem the databaseSystem for which this class should be initialized
     */
    public void init(DatabaseSystem databaseSystem) {
        try {
            String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
            String constsPath = rootPath + databaseSystem.propertyFileName;
            String typesPath = rootPath + databaseSystem.typesFileName;
            constsProps = new Properties();
            typesProps = new Properties();
            constsProps.load(new FileInputStream(constsPath));
            typesProps.load(new FileInputStream(typesPath));
            isInitialized = true;
        } catch (NullPointerException nE) {
            log.log(Level.SEVERE, "Not able to get Path to resource-directory: " + nE.getMessage());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Not able to load database system constants: " + e.getMessage());
        }
    }

    /**
     * Get a database specific constant
     *
     * @param key the key of the property
     * @return the property identified by the key or an empty string if no matching key was found
     */
    public String getConstant(String key) {
        return loadProperty(key, constsProps);
    }

    /**
     * Get a database specific sql type
     *
     * @param type the generic/intermediate SQL type which should be mapped into the database specific type
     * @return the {@code type} as String but it`s converted into the database specific type
     */
    public String getType(SQLType type) {
        return loadProperty(type.name(), typesProps);
    }

    /**
     * Get a database specific constant which contains placeholders.
     * Placeholders are replaced by the arguments
     *
     * @param key       the key of the property
     * @param arguments the arguments which should replace the placeholders
     * @return the property identified by the key containing the arguments or an empty string if no matching key was found
     */
    public String getPlaceholderConst(String key, Object... arguments) {
        if (!isInitialized) {
            log.log(Level.SEVERE, "The DbSysConstsLoader has not been initialized. Please do so first by calling init(databaseSystem).");
            return "";
        }
        return MessageFormat.format(constsProps.getProperty(key), arguments);
    }


    /**
     * Load a property from the according database specific .properties file
     *
     * @param key        the key of the property
     * @param properties the properties from which the property should be returned
     * @return the property identified by the key or an empty string if no matching key was found
     */
    private String loadProperty(String key, Properties properties) {
        if (!isInitialized) {
            log.log(Level.SEVERE, "The DbSysConstsLoader has not been initialized. Please do so first by calling init(databaseSystem).");
            return "";
        }
        final String prop = properties.getProperty(key);
        if (prop == null) {
            log.log(Level.SEVERE, "Property for key " + key + " not found. Returning empty string instead");
            return "";
        }
        return prop;
    }
}
