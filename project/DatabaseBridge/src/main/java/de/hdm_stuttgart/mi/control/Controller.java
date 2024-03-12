package de.hdm_stuttgart.mi.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
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

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * Defines what should happen when the Execute command is executed
     *
     * @param configFilePath the path to the configuration file the user specified
     */
    public void onExecute(String configFilePath, String ddlFilePath) {
        final ConnectionDetails sourceConnectionDetails = buildConnectionDetails(configFilePath, "source");
        final ConnectionDetails destinationConnectionDetails = buildConnectionDetails(configFilePath, "destination");

        if (sourceConnectionDetails == null || destinationConnectionDetails == null) {
            System.out.println("Exiting application due to empty connection configurations.");
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

        System.out.println("Reading source schema...");
        final Schema schema = schemaReader.readSchema(sourceConnectionDetails.getSchema());
        System.out.println("Reading source schema finished. Writing tables and users to destination DB...");
        schemaWriter.writeTablesAndUsersToDatabase(schema);
        System.out.println("Writing tables and users to destination DB finished. Reading data from source DB...");
        final ArrayList<TableData> data = dataReader.readData(schema);
        System.out.println("Reading data from source DB finished. Writing data to destination DB...");
        dataWriter.writeData(data);
        System.out.println("Writing data to destination DB finished. Wrting relations and views to destination DB...");
        schemaWriter.writeRelationsAndViewsToDatabase(schema);
        System.out.println("Wrting relations and views to destination DB finished.");
        System.out.println("Transferring finished. Closing connections...");

        //Save DDL script to separate file
        if (ddlFilePath != null) {
            StringBuilder ddlScript = new StringBuilder();
            ddlScript.append(schemaWriter.getDDLScript(schema));
            ddlScript.append(dataWriter.getDDLScript(data));
            try {
                Files.writeString(Path.of(ddlFilePath), ddlScript, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("DDL script saved in file: " + ddlFilePath);
            } catch (IOException e) {
                System.err.println("Error saving DDL script to file: " + e.getMessage());
            }
        }


        final ConnectionHandler sourceConnectionHandler = injector
                .getInstance(Key.get(ConnectionHandler.class, Names.named("sourceConnection")));
        final ConnectionHandler destinationConnectionHandler = injector
                .getInstance(Key.get(ConnectionHandler.class, Names.named("destinationConnection")));
        sourceConnectionHandler.closeConnection();
        destinationConnectionHandler.closeConnection();

        System.out.println("Connections closed. Application finished.");
    }

    /**
     * Defines what should happen when the NewConfigFile command is executed
     * Creates a new config file template at the configFileLocation
     *
     * @param configFileLocation the path to location the user wants the template file to be placed
     */
    public void onNewConfigFile(String configFileLocation) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("empty.json")) {
            //Write to new file
            try (FileOutputStream fos = new FileOutputStream(configFileLocation)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("The file couldn't be created at the provided path, please provide a valid path!");
                log.log(Level.SEVERE, "Not able to create new file" + configFileLocation + ":");
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Not able to find reference file: " + e.getMessage());
            System.out.println("Not able to find reference file");
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

            String databaseSystem = rootNode.get(connectionType + "Database").get("databaseSystem").asText();
            String databaseDriverName = rootNode.get(connectionType + "Database").get("databaseDriverName").asText();
            String databaseDriverJar = rootNode.get(connectionType + "Database").get("databaseDriverJar").asText();
            String hostAddress = rootNode.get(connectionType + "Database").get("hostAddress").asText();
            int port = rootNode.get(connectionType + "Database").get("port").asInt();
            String database = rootNode.get(connectionType + "Database").get("database").asText();
            String username = rootNode.get(connectionType + "Database").get("username").asText();
            String password = rootNode.get(connectionType + "Database").get("password").asText();

            boolean anyEmptyOrNull = databaseSystem.isBlank() || databaseDriverName.isBlank() || databaseDriverJar.isBlank() ||
                    hostAddress.isBlank() || username.isBlank() || password.isBlank();

            if (anyEmptyOrNull || (connectionType.equals("source") && database.isEmpty())) {
                System.out.println("Please check the config file again, one of the parameters is empty.");
                return null;
            }

            return new ConnectionDetails(
                    DatabaseSystem.valueOf(databaseSystem),
                    databaseDriverName,
                    databaseDriverJar,
                    hostAddress,
                    port,
                    database,
                    username,
                    password
            );
        } catch (IOException e) {
            log.log(Level.SEVERE, "Not able to read json-tree from config file " + configFilePath + ":" + e.getMessage());
        }
        return null;
    }
}
