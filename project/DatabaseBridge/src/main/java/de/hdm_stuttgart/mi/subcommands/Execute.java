package de.hdm_stuttgart.mi.subcommands;

import de.hdm_stuttgart.mi.control.Controller;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;

/**
 * Picocli Command, executes the database transfer, if possible
 */
@Command(
        name = "execute",
        description = "Execute the database transfer, all database parameters need to be specified in the configuration file"
)
public class Execute implements Runnable {
    @Parameters(description = "path to your configuration file")
    private String configurationFile;

    @Option(names = {"-s", "-script"}, description = "the created DDL script is saved to this file")
    private String DDLFile;

    @Option(names = "--help", usageHelp = true, description = "display this help and exit")
    boolean help;

    @Override
    public void run() {
        final Controller controller = new Controller();
        controller.onExecute(configurationFile, DDLFile);
    }
}
