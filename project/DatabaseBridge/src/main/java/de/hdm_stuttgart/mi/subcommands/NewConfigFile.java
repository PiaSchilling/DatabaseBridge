package de.hdm_stuttgart.mi.subcommands;

import picocli.CommandLine;

/**
 * Piccocli Command, creates new config file, with required structure, values are empty
 */
@CommandLine.Command(
        name = "new",
        description = "Creates a new configuration file at the specified path"
)
public class NewConfigFile implements Runnable{
    @CommandLine.Parameters() private String fileLocation;

    @Override
    public void run() {
        //TODO: Implement real functionality
        System.out.println("I'm the new-config Command");
        if (fileLocation != null) {
            System.out.println("creating new file in: " + fileLocation);
        }
    }
}

