package de.hdm_stuttgart.mi.subcommands;

import de.hdm_stuttgart.mi.control.Controller;
import picocli.CommandLine;


/**
 * Picocli Command, creates new config file, with required structure, values are empty
 */
@CommandLine.Command(
        name = "new",
        description = "Create a correctly formatted configuration file with empty values at the given path"
)
public class NewConfigFile implements Runnable {
    @CommandLine.Parameters(description = "path to new configuration file")
    private String fileLocation;

    @CommandLine.Option(names = "--help", usageHelp = true, description = "display this help and exit")
    boolean help;

    @Override
    public void run() {
        final Controller controller = new Controller();
        controller.onNewConfigFile(fileLocation);
    }
}

