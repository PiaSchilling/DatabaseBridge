package de.hdm_stuttgart.mi;


import com.google.inject.Guice;
import com.google.inject.Injector;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.model.DatabaseSystem;
import de.hdm_stuttgart.mi.di.ConnectModule;
import de.hdm_stuttgart.mi.di.SchemaReadModule;
import de.hdm_stuttgart.mi.read.api.SchemaReader;
import de.hdm_stuttgart.mi.read.model.*;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        final ConnectionDetails sourceDetailsMariaDB = new ConnectionDetails(
                DatabaseSystem.MARIADB,
                "org.mariadb.jdbc.Driver",
                "src/main/resources/jar/mariadb-java-client-3.3.0.jar",
                "localhost",
                3307,
                "test",
                "root",
                "example");

        final ConnectionDetails sourceDetailsMySql = new ConnectionDetails(DatabaseSystem.MYSQL,
                "com.mysql.cj.jdbc.Driver",
                "src/main/resources/jar/mysql-connector-j-8.0.33.jar",
                "localhost",
                3306,
                "movies",
                "root",
                "example");


        final ConnectionDetails sourceDetailsPostgres = new ConnectionDetails(DatabaseSystem.POSTGRES,
                "org.postgresql.Driver",
                "src/main/resources/jar/postgresql-42.7.1.jar",
                "localhost",
                5432,
                "movies",
                "postgres",
                "example");

        // Just dummy data, destination db currently not in use!
        final ConnectionDetails destinationDetailsPostgres = new ConnectionDetails(DatabaseSystem.POSTGRES,
                "org.postgresql.Driver",
                "src/main/resources/jar/postgresql-42.7.1.jar",
                "localhost",
                5432,
                "movies",
                "postgres",
                "example");

        Injector injector = Guice.createInjector(new ConnectModule(sourceDetailsPostgres, destinationDetailsPostgres),
                new SchemaReadModule());

        SchemaReader schemaReader = injector.getInstance(SchemaReader.class);
        final Schema schema = schemaReader.readSchema(sourceDetailsPostgres.getSchema());

        System.out.println(schema);

        //Test DataReader print out values
        ArrayList<CachedRowSet> tables = new ArrayList<>();
        for(Table table : schema.tables()) {
            ArrayList<String> columns = new ArrayList<>();
            for(int columnIndex = 0; columnIndex < table.columns().size(); columnIndex++) {
                columns.add(table.columns().get(columnIndex).name());
            }
            System.out.println(columns);
            try(CachedRowSet cachedRowSet = table.data()) {
                while (cachedRowSet.next()) {
                    for (String column : columns) {
                        System.out.print(cachedRowSet.getString(column) + ",");
                    }
                    System.out.println();
                }
            } catch(SQLException e) {
                System.out.println("Error while trying to print table data: " + e.getMessage());
            }

        }

        // TODO close DB connection
    }
}