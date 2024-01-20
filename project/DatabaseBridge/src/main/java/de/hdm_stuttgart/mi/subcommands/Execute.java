package de.hdm_stuttgart.mi.subcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.model.DatabaseSystem;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;

/**
 * Picocli Command, executes the database transfer, if possible
 */
@Command(
        name = "execute",
        description = "Executes the database transfer"
)
public class Execute implements Runnable {
    @Parameters() private String fileLocation;

    @Override
    public void run() {

        //At the moment: try to connect to source database
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(fileLocation));

            // Create ConnectionDetails Instance for Source Database from Json file
            ConnectionDetails sourceDatabase = new ConnectionDetails(
                    DatabaseSystem.valueOf(rootNode.get("sourceDatabase").get("databaseSystem").asText()),
                    rootNode.get("sourceDatabase").get("databaseDriverName").asText(),
                    rootNode.get("sourceDatabase").get("databaseDriverJar").asText(),
                    rootNode.get("sourceDatabase").get("hostAddress").asText(),
                    rootNode.get("sourceDatabase").get("port").asInt(),
                    rootNode.get("sourceDatabase").get("database").asText(),
                    rootNode.get("sourceDatabase").get("username").asText(),
                    rootNode.get("sourceDatabase").get("password").asText()
            );

            //Connect Database to SourceConnectionHandler and create Connection
           /* SourceConnectionHandler.getInstance().connectDatabase(sourceDatabase);
            boolean connected = SourceConnectionHandler.getInstance().connectionActive();
            System.out.println("Is connected: " + connected);*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
