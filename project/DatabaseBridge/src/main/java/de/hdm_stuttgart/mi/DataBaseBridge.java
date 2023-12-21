package de.hdm_stuttgart.mi;

import de.hdm_stuttgart.mi.subcommands.Execute;
import de.hdm_stuttgart.mi.subcommands.NewConfigFile;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

@Command(
        name= "databasebridge",
        description= "Copies a source database into destination database, all parameters need to be specified in a configuration file",
        subcommands = {
                Execute.class, NewConfigFile.class, HelpCommand.class,
        }
)
public class DataBaseBridge implements Runnable {
    public static void main(String[] args) {
        //Only for Testing at the moment, provide arguments/ subcommands
        String[] args2 = {"help"};
        //Run the main class
        int exitCode = new CommandLine(new DataBaseBridge()).execute(args2);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("This is the the main command");
    }

}