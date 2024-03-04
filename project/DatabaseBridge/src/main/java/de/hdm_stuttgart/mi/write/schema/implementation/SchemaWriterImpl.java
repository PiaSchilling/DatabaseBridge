package de.hdm_stuttgart.mi.write.schema.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.read.schema.model.*;
import de.hdm_stuttgart.mi.util.StatementExecutor;
import de.hdm_stuttgart.mi.write.schema.api.SchemaWriter;

import java.sql.Connection;
import java.util.ArrayList;

public class SchemaWriterImpl implements SchemaWriter {

    private final Connection destinationConnection;

    @Inject
    public SchemaWriterImpl(@Named("destinationConnection") ConnectionHandler destinationConnectionHandler) {
        this.destinationConnection = destinationConnectionHandler.getConnection();
    }

    @Override
    public String getDDLScript(Schema schema) {
        final String schemaName = schema.name();
        final ArrayList<Table> tables = schema.tables();

        return getCreateUsersStatement(schema.users()) + "\n" +
                getDropSchemaStatement(schemaName) + "\n" +
                getDropTablesStatement(schemaName, tables) + "\n" +
                getCreateSchemaStatement(schemaName) + "\n" +
                getCreateTablesStatement(schemaName, tables) + "\n" +
                getCreateRelationsStatement(schemaName, tables) + "\n" +
                getCreateViewsStatement(schemaName, schema.views());
    }

    @Override
    public void writeSchemaToDatabase(Schema schema) {
        writeTablesAndUsersToDatabase(schema);
        writeRelationsAndViewsToDatabase(schema);
    }

    @Override
    public void writeTablesAndUsersToDatabase(Schema schema) {
        final String schemaName = schema.name();
        final ArrayList<Table> tables = schema.tables();
        executeCreateUsers(schema.users());
        executeDropSchema(schemaName);
        executeDropTables(schemaName, tables);
        executeCreateSchema(schemaName);
        executeCreateTables(schemaName, tables);
    }

    @Override
    public void writeRelationsAndViewsToDatabase(Schema schema) {
        final String schemaName = schema.name();
        final ArrayList<Table> tables = schema.tables();
        executeCreateRelations(schemaName, tables);
        executeCreateViews(schemaName, schema.views());
    }

    // - - - - - - Schemas - - - - -
    private String getDropSchemaStatement(String schemaName) {
        return SchemaStatementBuilder.dropSchemaStatement(schemaName);
    }

    public void executeDropSchema(String schemaName) {
        StatementExecutor.executeWrite(destinationConnection, getDropSchemaStatement(schemaName));
    }

    private String getCreateSchemaStatement(String schemaName) {
        return SchemaStatementBuilder.createSchemaStatement(schemaName);
    }

    public void executeCreateSchema(String schemaName) {
        StatementExecutor.executeWrite(destinationConnection, getCreateSchemaStatement(schemaName));
    }

    // - - - - - - Users - - - - -
    private String getCreateUsersStatement(ArrayList<User> users) {
        StringBuilder builder = new StringBuilder();
        for (User user : users
        ) {
            builder.append(SchemaStatementBuilder.createUserStatement(user))
                    .append("\n");
        }
        return builder.toString();
    }

    private String getSingleCreateUsersStatement(User user) {
        return SchemaStatementBuilder.createUserStatement(user);
    }

    public void executeCreateUsers(ArrayList<User> users) {
        for (User user : users
        ) {
            StatementExecutor.executeWrite(destinationConnection, getSingleCreateUsersStatement(user));
        }
    }

    // - - - - - - Tables - - - - -
    private String getDropTablesStatement(String schemaName, ArrayList<Table> tables) {
        StringBuilder builder = new StringBuilder();
        for (Table table : tables
        ) {
            builder.append(SchemaStatementBuilder.dropTableStatement(table, schemaName))
                    .append("\n");
        }
        return builder.toString();
    }

    private String getSingleDropTableStatement(String schemaName, Table table) {
        return SchemaStatementBuilder.dropTableStatement(table, schemaName);
    }

    public void executeDropTables(String schemaName, ArrayList<Table> tables) {
        for (Table table : tables
        ) {
            StatementExecutor.executeWrite(destinationConnection, getSingleDropTableStatement(schemaName, table));
        }
    }

    private String getSingleCreateTableStatement(String schemaName, Table table) {
        return SchemaStatementBuilder.createTableStatement(table, schemaName);
    }

    public void executeCreateTables(String schemaName, ArrayList<Table> tables) {
        for (Table table : tables
        ) {
            StatementExecutor.executeWrite(destinationConnection, getSingleCreateTableStatement(schemaName, table));
        }
    }

    public String getCreateTablesStatement(String schemaName, ArrayList<Table> tables) {
        StringBuilder builder = new StringBuilder();
        for (Table table : tables
        ) {
            builder.append(SchemaStatementBuilder.createTableStatement(table, schemaName))
                    .append("\n\n");
        }
        return builder.toString();
    }

    // - - - - - - Relations - - - - -
    private String getSingleCreateRelationsStatement(String schemaName, Table table, FkRelation relation) {
        return SchemaStatementBuilder.singleAlterTableAddFkRelationStatement(table, relation, schemaName);
    }

    public String getCreateRelationsStatement(String schemaName, ArrayList<Table> tables) {
        StringBuilder builder = new StringBuilder();
        for (Table table : tables
        ) {
            builder.append(SchemaStatementBuilder.alterTableAddFkRelationStatement(table, schemaName));
        }
        return builder.toString();
    }

    public void executeCreateRelations(String schemaName, ArrayList<Table> tables) {
        for (Table table : tables
        ) {
            for (FkRelation relation : table.importedFkRelations()) {
                StatementExecutor.executeWrite(destinationConnection,
                        getSingleCreateRelationsStatement(schemaName, table, relation));
            }
        }
    }

    // - - - - - - Views - - - - -
    private String getSingleCreateViewStatement(String schemaName, View view) {
        return SchemaStatementBuilder.createViewStatement(view, schemaName);
    }

    public String getCreateViewsStatement(String schemaName, ArrayList<View> views) {
        StringBuilder builder = new StringBuilder();
        for (View view : views
        ) {
            builder.append(SchemaStatementBuilder.createViewStatement(view, schemaName))
                    .append("\n");
        }
        return builder.toString();
    }

    public void executeCreateViews(String schemaName, ArrayList<View> views) {
        for (View view : views
        ) {
            StatementExecutor.executeWrite(destinationConnection, getSingleCreateViewStatement(schemaName, view));
        }

    }

}
