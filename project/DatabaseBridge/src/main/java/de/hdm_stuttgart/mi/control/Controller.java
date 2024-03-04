package de.hdm_stuttgart.mi.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.model.DatabaseSystem;
import de.hdm_stuttgart.mi.di.ConnectModule;
import de.hdm_stuttgart.mi.di.ReadModule;
import de.hdm_stuttgart.mi.di.WriteModule;
import de.hdm_stuttgart.mi.read.data.api.DataReader;
import de.hdm_stuttgart.mi.read.data.model.TableData;
import de.hdm_stuttgart.mi.read.schema.api.SchemaReader;
import de.hdm_stuttgart.mi.read.schema.model.Schema;
import de.hdm_stuttgart.mi.util.consts.DestinationDbSysConstsLoader;
import de.hdm_stuttgart.mi.util.consts.SourceDbSysConstsLoader;
import de.hdm_stuttgart.mi.write.data.api.DataWriter;
import de.hdm_stuttgart.mi.write.schema.api.SchemaWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * Defines what should happen when the Execute command is executed
     * Currently runs only schema read
     *
     * @param configFilePath the path to the configuration file the user specified
     */
    public void onExecute(String configFilePath) {
        final ConnectionDetails sourceConnectionDetails = buildConnectionDetails(configFilePath, "source");
        final ConnectionDetails destinationConnectionDetails = buildConnectionDetails(configFilePath, "destination");

        if (sourceConnectionDetails == null || destinationConnectionDetails == null) {
            return;
        }

        final Injector injector = Guice.createInjector(
                new ConnectModule(sourceConnectionDetails, destinationConnectionDetails),
                new ReadModule(),
                new WriteModule());

        SourceDbSysConstsLoader.INSTANCE().init(sourceConnectionDetails.getDatabaseSystem());
        DestinationDbSysConstsLoader.INSTANCE().init(destinationConnectionDetails.getDatabaseSystem());

        final SchemaReader schemaReader = injector.getInstance(SchemaReader.class);
        final SchemaWriter schemaWriter = injector.getInstance(SchemaWriter.class);
        final DataReader dataReader = injector.getInstance(DataReader.class);
        final DataWriter dataWriter = injector.getInstance(DataWriter.class);

        final Schema schema = schemaReader.readSchema(sourceConnectionDetails.getSchema());
        schemaWriter.writeTablesToDatabase(schema);
        final ArrayList<TableData> data = dataReader.readData(schema);
        dataWriter.writeData(data);
        schemaWriter.writeRelationsAndViewsToDatabase(schema);
        System.out.println("Finished");
    }

    /**
     * Defines what should happen when the NewConfigFile command is executed
     * Creates a new config file template at the configFileLocation
     *
     * @param configFileLocation the path to location the user wants the template file to be placed
     */
    public void onNewConfigFile(String configFileLocation) {
        Path sourcePath = Path.of("src/main/resources/empty.json");
        Path destinationPath = Path.of(configFileLocation);

        try {
            // Copy the file using Files.copy method
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("JSON file created successfully at: " + destinationPath);
        } catch (NoSuchFileException e) {
            System.out.println("The file couldn't be created at the provided path, please provide a valid path!");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Not able to read config file " + configFileLocation + ":" + e.getMessage());
        }
    }

    /**
     * Build connectionDetails from a config file
     *
     * @param configFilePath the path to the config file
     * @param connectionType "source" or "destination"
     * @return a ConnectionDetails object containing all relevant connect info for either the source or destination database
     */
    private ConnectionDetails buildConnectionDetails(String configFilePath, String connectionType) {
        if (!(connectionType.equals("source") | connectionType.equals("destination"))) {
            log.log(Level.SEVERE, connectionType + " is not a valid connection Type. It should either be source or destination");
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(configFilePath));
            return new ConnectionDetails(
                    DatabaseSystem.valueOf(rootNode.get(connectionType + "Database").get("databaseSystem").asText()),
                    rootNode.get(connectionType + "Database").get("databaseDriverName").asText(),
                    rootNode.get(connectionType + "Database").get("databaseDriverJar").asText(),
                    rootNode.get(connectionType + "Database").get("hostAddress").asText(),
                    rootNode.get(connectionType + "Database").get("port").asInt(),
                    rootNode.get(connectionType + "Database").get("database").asText(),
                    rootNode.get(connectionType + "Database").get("username").asText(),
                    rootNode.get(connectionType + "Database").get("password").asText()
            );
            // TODO check if any values are empty that may not be empty
        } catch (IOException e) {
            log.log(Level.SEVERE, "Not able to read json-tree from config file " + configFilePath + ":" + e.getMessage());
        }
        return null;
    }
}
