package de.hdm_stuttgart.mi.util;

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
public class DbSysConstsLoader {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private static DbSysConstsLoader INSTANCE;

    private Properties databaseSystemsConsts;

    private DbSysConstsLoader() {
        init();
    }

    public static DbSysConstsLoader INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DbSysConstsLoader();
        }
        return INSTANCE;
    }

    /**
     * Initialize the provider by loading the db_sys_consts.properties into databaseSystemsConsts
     */
    private void init() {
        try {
            String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
            String appConfigPath = rootPath + "db_sys_consts.properties";
            databaseSystemsConsts = new Properties();
            databaseSystemsConsts.load(new FileInputStream(appConfigPath));
        } catch (NullPointerException nE) {
            log.log(Level.SEVERE, "Not able to get Path to resource-directory: " + nE.getMessage());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Not able to load database system constants: " + e.getMessage());
        }
    }

    /**
     * Get a property from db_sys_consts.properties
     *
     * @param key the key of the property
     * @return the property identified by the key or an empty string if no matching key was found
     */
    public String getProperty(String key) {
        final String prop = databaseSystemsConsts.getProperty(key);
        if (prop == null) {
            log.log(Level.SEVERE, "Property for key " + key + " not found. Returning empty string instead");
            return "";
        }
        return prop;
    }

    /**
     * Get a property from db_sys_consts.properties which contains placeholders.
     * Placeholders are replaced by the arguments
     *
     * @param key       the key of the property
     * @param arguments the arguments which should replace the placeholders
     * @return the property identified by the key containing the arguments or an empty string if no matching key was found
     */
    public String getPlaceholderProperty(String key, Object... arguments) {
        return MessageFormat.format(databaseSystemsConsts.getProperty(key), arguments);
    }
}
