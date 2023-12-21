package de.hdm_stuttgart.mi.subcommands;

import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Picocli Command, creates new config file, with required structure, values are empty
 */
@CommandLine.Command(
        name = "new",
        description = "Creates a new configuration file at the specified path"
)
public class NewConfigFile implements Runnable{
    @CommandLine.Parameters() private String fileLocation;

    @Override
    public void run() {

        Path sourcePath = Path.of("src/main/resources/empty.json");
        Path destinationPath = Path.of(fileLocation);

        try {
            // Copy the file using Files.copy method
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("JSON file created successfully at: " + destinationPath);
        } catch(NoSuchFileException e) {
            System.out.println("The file couldn't be created at the provided path, please provide a valid path!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

