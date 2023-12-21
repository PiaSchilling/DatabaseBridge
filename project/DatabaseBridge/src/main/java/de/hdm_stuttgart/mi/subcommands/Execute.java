package de.hdm_stuttgart.mi.subcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hdm_stuttgart.mi.connect.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.DatabaseSystem;
import de.hdm_stuttgart.mi.connect.SourceConnectionHandler;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

        //Try to read data from source database
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(fileLocation));

            // Create ConnectionDetails Instance for Source Database from Json file
            ConnectionDetails sourceDatabase = new ConnectionDetails(
                    DatabaseSystem.valueOf(rootNode.get("sourceDatabase").get("databaseSystem").asText()),
                    rootNode.get("sourceDatabase").get("hostAddress").asText(),
                    rootNode.get("sourceDatabase").get("port").asInt(),
                    rootNode.get("sourceDatabase").get("database").asText(),
                    rootNode.get("sourceDatabase").get("username").asText(),
                    rootNode.get("sourceDatabase").get("password").asText()
            );

            //Connect Database to SourceConnectionHandler and create Connection
            SourceConnectionHandler.getInstance().connectDatabase(sourceDatabase);
            Connection connection = SourceConnectionHandler.getInstance().getConnection();
            Statement stmt = connection.createStatement();

            //For now Test if data can be read
            //TODO Read Schema, ...
            ResultSet result = stmt.executeQuery("SELECT * FROM movies.movie");

            while (result.next()) {
                System.out.println(result.getString("movie_name"));
            }
            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
