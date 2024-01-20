package de.hdm_stuttgart.mi.subcommands;

import de.hdm_stuttgart.mi.control.Controller;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

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
        final Controller controller = new Controller(); // TODO inject controller
        controller.onExecute(fileLocation);
    }
}
