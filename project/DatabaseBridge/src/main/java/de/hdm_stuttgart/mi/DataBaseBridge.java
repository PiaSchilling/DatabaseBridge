package de.hdm_stuttgart.mi;

import de.hdm_stuttgart.mi.subcommands.Execute;
import de.hdm_stuttgart.mi.subcommands.NewConfigFile;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

@Command(
        name= "databasebridge",
        description= "Easily migrate database schemas between systems",
        subcommands = {
                Execute.class, NewConfigFile.class, HelpCommand.class,
        }
)
public class DataBaseBridge implements Runnable {
    public static void main(String[] args) {
        //Only for Testing at the moment, provide arguments/ subcommands
        String[] args2 = {"execute", "src/main/resources/test2.json", "-s", "src/main/resources/ddl.text"};
        //String[] args2 = {"new", "--help"};
        //Run the main class
        int exitCode = new CommandLine(new DataBaseBridge()).execute(args2);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("Welcome to DataBaseBridge!");
        System.out.println("DataBaseBridge helps you seamlessly migrate databases from one system to another.");
        System.out.println("Supported systems: MySQL, PostgreSQL and MariaDB.\n");
        System.out.println("Use 'databasebridge help' for usage information.");
    }
}