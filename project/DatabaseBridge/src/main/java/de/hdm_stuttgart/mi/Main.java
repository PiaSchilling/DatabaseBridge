package de.hdm_stuttgart.mi;


import com.google.inject.Guice;
import com.google.inject.Injector;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.model.DatabaseSystem;
import de.hdm_stuttgart.mi.di.ConnectModule;
import de.hdm_stuttgart.mi.di.SchemaReadModule;
import de.hdm_stuttgart.mi.read.schema.api.SchemaReader;
import de.hdm_stuttgart.mi.read.schema.model.Schema;
import de.hdm_stuttgart.mi.util.DbSysConstsLoader;
import de.hdm_stuttgart.mi.write.schema.SchemaWriter;


public class Main {
    public static void main(String[] args) {
        final ConnectionDetails sourceDetailsMariaDB = new ConnectionDetails(
                DatabaseSystem.MARIADB,
                "org.mariadb.jdbc.Driver",
                "src/main/resources/jar/mariadb-java-client-3.3.0.jar",
                "localhost",
                3307,
                "travel",
                "root",
                "example");

        final ConnectionDetails sourceDetailsMySql = new ConnectionDetails(DatabaseSystem.MYSQL,
                "com.mysql.cj.jdbc.Driver",
                "src/main/resources/jar/mysql-connector-j-8.0.33.jar",
                "localhost",
                3306,
                "employees",
                "root",
                "example");

        final ConnectionDetails sourceDetailsPostgres = new ConnectionDetails(DatabaseSystem.POSTGRES,
                "org.postgresql.Driver",
                "src/main/resources/jar/postgresql-42.7.1.jar",
                "localhost",
                5432,
                "ecommerce",
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

        // TODO move logic to controller once finished testing
        final ConnectionDetails testSourceDetails = sourceDetailsMySql;


        Injector injector = Guice.createInjector(new ConnectModule(testSourceDetails, destinationDetailsPostgres),
                new SchemaReadModule());
        DbSysConstsLoader.INSTANCE().init(testSourceDetails.getDatabaseSystem());

        SchemaReader schemaReader = injector.getInstance(SchemaReader.class);
        final Schema schema = schemaReader.readSchema(testSourceDetails.getSchema());

        // System.out.println(schema);

        SchemaWriter schemaWriter = new SchemaWriter();
        schemaWriter.writeSchema(schema);


     /*   final ArrayList<Constraint> c = new ArrayList();
        final Constraint constraint = new Constraint(ConstraintType.NOT_NULL);
        final Constraint constraint2 = new Constraint(ConstraintType.DEFAULT, "test");
        c.add(constraint);
        c.add(constraint2);

        final Column test = new Column("user", SQLType.VARCHAR, 64, c);
        System.out.println(test.asStatement());

        final FkRelation relation = new FkRelation("users", "id", "user_idRef", DeleteUpdateRule.IMPORTED_KEY_CASCADE, DeleteUpdateRule.IMPORTED_NO_ACTION);
        System.out.println(relation.asStatement());*/

        // TODO close DB connection
    }
}