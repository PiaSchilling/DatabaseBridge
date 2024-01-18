package de.hdm_stuttgart.mi;


import com.google.inject.Guice;
import com.google.inject.Injector;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.model.DatabaseSystem;
import de.hdm_stuttgart.mi.di.BasicModule;
import de.hdm_stuttgart.mi.read.api.UsersReader;
import de.hdm_stuttgart.mi.read.model.User;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Usage example, schema movies must exist!
        final ConnectionDetails sourceDetails = new ConnectionDetails(DatabaseSystem.MYSQL,
                "localhost",
                3306,
                "employees",
                "root",
                "example");

        // Just dummy data, destination db currently not in use!
        final ConnectionDetails destinationDetails = new ConnectionDetails(DatabaseSystem.POSTGRES,
                "localhost",
                8888,
                "example",
                "root",
                "example");

        Injector injector = Guice.createInjector(new BasicModule(sourceDetails, destinationDetails));

        //SchemaReader schemaReader = injector.getInstance(SchemaReader.class);
        //final ArrayList<Table> tables = schemaReader.readSchema(sourceDetails.getSchema()).getTables();
        //System.out.println(Arrays.toString(tables.toArray()));

        UsersReader usersReader = injector.getInstance(UsersReader.class);
        final ArrayList<User> users = usersReader.readUsers();
        System.out.println(Arrays.toString(users.toArray()));

        // TODO close DB connection
        // TODO split schemas?

    }
}