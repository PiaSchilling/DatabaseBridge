package de.hdm_stuttgart.mi;


import com.google.inject.Guice;
import com.google.inject.Injector;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.model.DatabaseSystem;
import de.hdm_stuttgart.mi.di.BasicModule;
import de.hdm_stuttgart.mi.read.api.SchemaReader;
import de.hdm_stuttgart.mi.read.model.*;


public class Main {
    public static void main(String[] args) {
        final ConnectionDetails sourceDetailsMariaDB = new ConnectionDetails(DatabaseSystem.MARIADB,
                "localhost",
                3307,
                "travel",
                "root",
                "example");

        final ConnectionDetails sourceDetailsMySql = new ConnectionDetails(DatabaseSystem.MYSQL,
                "localhost",
                3306,
                "employees",
                "root",
                "example");

        final ConnectionDetails sourceDetailsPostgres = new ConnectionDetails(DatabaseSystem.POSTGRES,
                "localhost",
                5432,
                "test",
                "postgres",
                "example");

        // Just dummy data, destination db currently not in use!
        final ConnectionDetails destinationDetails = new ConnectionDetails(DatabaseSystem.POSTGRES,
                "localhost",
                8888,
                "example",
                "root",
                "example");

        Injector injector = Guice.createInjector(new BasicModule(sourceDetailsPostgres, destinationDetails));

        SchemaReader schemaReader = injector.getInstance(SchemaReader.class);
        final Schema schema = schemaReader.readSchema(sourceDetailsPostgres.getSchema());

        System.out.println(schema);

        // TODO close DB connection
    }
}