package de.hdm_stuttgart.mi;


import com.google.inject.Guice;
import com.google.inject.Injector;
import de.hdm_stuttgart.mi.connect.implementation.ConnectionHandlerImpl;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.model.ConnectionType;
import de.hdm_stuttgart.mi.connect.model.DatabaseSystem;
import de.hdm_stuttgart.mi.data.ReadData;
import de.hdm_stuttgart.mi.di.ConnectModule;
import de.hdm_stuttgart.mi.di.SchemaReadModule;
import de.hdm_stuttgart.mi.read.api.SchemaReader;
import de.hdm_stuttgart.mi.read.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


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
                "test",
                "root",
                "example");


        final ConnectionDetails sourceDetailsPostgres = new ConnectionDetails(DatabaseSystem.POSTGRES,
                "org.postgresql.Driver",
                "src/main/resources/jar/postgresql-42.7.1.jar",
                "localhost",
                5432,
                "test",
                "postgres",
                "example");

        // Just dummy data, destination db currently not in use!
        final ConnectionDetails destinationDetailsPostgres = new ConnectionDetails(DatabaseSystem.POSTGRES,
                "org.postgresql.Driver",
                "src/main/resources/jar/postgresql-42.7.1.jar",
                "localhost",
                5432,
                "test",
                "postgres",
                "example");

        Injector injector = Guice.createInjector(new ConnectModule(sourceDetailsMySql, destinationDetailsPostgres),
                new SchemaReadModule());

        SchemaReader schemaReader = injector.getInstance(SchemaReader.class);
        final Schema schema = schemaReader.readSchema(sourceDetailsMySql.getSchema());

        System.out.println(schema);

        final ConnectionHandlerImpl sourceConnectionHandler = new ConnectionHandlerImpl(ConnectionType.SOURCE, sourceDetailsMySql);
        System.out.println(sourceConnectionHandler.connectionActive());
        final Connection sourceConnection =  sourceConnectionHandler.getConnection();
        ReadData readData = new ReadData(schema, sourceConnection);
        ArrayList<ResultSet> tables = readData.readTableData();

        try {
            for (int i = 0; i< tables.size(); i++) {
                ArrayList<String> columns = new ArrayList<>();
                //Get column names of current table
                for(int columnIndex = 0; columnIndex < schema.tables().get(i).columns().size(); columnIndex++) {
                    columns.add(schema.tables().get(i).columns().get(columnIndex).name());
                }
                ResultSet rs = tables.get(i);
                System.out.println(columns);
                while (rs.next()) {
                    for (String column : columns) {
                        System.out.print(rs.getString(column) + ",");
                    }
                    System.out.println();
                    //System.out.println(rs.getString("order_id"));
                }
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }


        /*
        try{
            final ConnectionHandlerImpl sourceConnectionHandler = new ConnectionHandlerImpl(ConnectionType.SOURCE, sourceDetailsPostgres);
            System.out.println(sourceConnectionHandler.connectionActive());
            final Connection sourceConnection =  sourceConnectionHandler.getConnection();
            final Statement stmt = sourceConnection.createStatement();
            //schema.tables();
            //stmt.executeUpdate("INSERT INTO Customers " + "VALUES (1002, 'McBeal', 'Ms.', 'Boston', 2004)");
            System.out.println(schema.tables().get(0).name());
            System.out.println(sourceConnectionHandler.getConnectionDetails().getSchema());
            ResultSet rs = stmt.executeQuery("SELECT * FROM test.orders");
            //System.out.println(rs.getArray(1));
            String[] columns = {"order_id", "user_id", "order_date", "total_amount"};
            System.out.println(schema.tables().get(0).columns().get(2).name());

            while (rs.next()) {
                for(String column : columns) {
                    System.out.print(rs.getString(column) + ",");
                }
                System.out.println();
                //System.out.println(rs.getString("order_id"));
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
*/
        // TODO close DB connection
    }
}