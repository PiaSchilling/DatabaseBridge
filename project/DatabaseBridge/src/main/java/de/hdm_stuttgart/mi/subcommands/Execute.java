package de.hdm_stuttgart.mi.subcommands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Piccocli Command, executes the database transfer, if possible
 */
@Command(
        name = "execute",
        description = "Executes the database transfer"
)
public class Execute implements Runnable {
    @Parameters() private String fileLocation;

    @Override
    public void run() {
        //TODO: Implement real functionality
        System.out.println("This is the execute command");
        if (fileLocation != null) {
            System.out.println("Processing file: " + fileLocation);
        }
    }
}
