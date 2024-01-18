package de.hdm_stuttgart.mi;


import com.google.inject.Guice;
import com.google.inject.Injector;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.model.DatabaseSystem;
import de.hdm_stuttgart.mi.di.BasicModule;
import de.hdm_stuttgart.mi.read.api.PrivilegeReader;
import de.hdm_stuttgart.mi.read.api.SchemaReader;
import de.hdm_stuttgart.mi.read.api.UsersReader;
import de.hdm_stuttgart.mi.read.model.ColumnPrivilege;
import de.hdm_stuttgart.mi.read.model.Privilege;
import de.hdm_stuttgart.mi.read.model.Table;
import de.hdm_stuttgart.mi.read.model.User;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Usage example, schema movies must exist!
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

        Injector injector = Guice.createInjector(new BasicModule(sourceDetailsMariaDB, destinationDetails));

        System.out.println("TABLES");
        SchemaReader schemaReader = injector.getInstance(SchemaReader.class);
        final ArrayList<Table> tables = schemaReader.readSchema(sourceDetailsMariaDB.getSchema()).getTables();
        System.out.println(Arrays.toString(tables.toArray()));

        System.out.println("\n\n\n");

        System.out.println("USERS");
        UsersReader usersReader = injector.getInstance(UsersReader.class);
        final ArrayList<User> users = usersReader.readUsers();
        System.out.println(Arrays.toString(users.toArray()));

        System.out.println("\n\n\n");


        PrivilegeReader privilegeReader = injector.getInstance(PrivilegeReader.class);
        final ArrayList<Privilege> p1 = privilegeReader.readTablePrivileges();
        final ArrayList<ColumnPrivilege> p2 = privilegeReader.readColumnPrivileges();

        System.out.println("TABLE PRIVILEGES");
        System.out.println(Arrays.toString(p1.toArray()));

        System.out.println("COLUMN PRIVILEGES");
        System.out.println(Arrays.toString(p2.toArray()));

        // TODO close DB connection
        // TODO split schemas?

    }
}