package de.hdm_stuttgart.mi.subcommands;

import de.hdm_stuttgart.mi.control.Controller;
import picocli.CommandLine;


/**
 * Picocli Command, creates new config file, with required structure, values are empty
 */
@CommandLine.Command(
        name = "new",
        description = "Creates a new configuration file at the specified path"
)
public class NewConfigFile implements Runnable {
    @CommandLine.Parameters()
    private String fileLocation;

    @Override
    public void run() {
        final Controller controller = new Controller(); // TODO inject
        controller.onNewConfigFile(fileLocation);
    }
}

