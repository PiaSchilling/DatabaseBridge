package de.hdm_stuttgart.mi.write.schema.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.read.schema.model.Schema;
import de.hdm_stuttgart.mi.read.schema.model.Table;
import de.hdm_stuttgart.mi.read.schema.model.View;
import de.hdm_stuttgart.mi.util.StatementExecutor;
import de.hdm_stuttgart.mi.write.schema.api.SchemaWriter;

import java.sql.Connection;
import java.util.ArrayList;

public class SchemaWriterImpl implements SchemaWriter {

    // TODO comment this class
    private final Connection destinationConnection;

    @Inject
    public SchemaWriterImpl(@Named("destinationConnection") ConnectionHandler destinationConnectionHandler) {
        this.destinationConnection = destinationConnectionHandler.getConnection();
    }

    public String getDDLScript(Schema schema) {
        final String schemaName = schema.name();
        final ArrayList<Table> tables = schema.tables();

        return getDropSchemaStatement(schemaName) +
                getDropTablesStatement(schemaName, tables) +
                getCreateSchemaStatement(schemaName) +
                getCreateTablesStatement(schemaName, tables) +
                getCreateRelationsStatement(schemaName, tables) +
                getCreateViewsStatement(schemaName, schema.views());
    }

    public void executeDDLScript(Schema schema) {
        final String schemaName = schema.name();
        final ArrayList<Table> tables = schema.tables();

        executeDropSchema(schemaName);
        executeDropTables(schemaName, tables);
        executeCreateSchema(schemaName);
        executeCreateTables(schemaName, tables);
        executeCreateRelations(schemaName, tables);
        executeCreateViews(schemaName, schema.views());
    }

    private String getDropSchemaStatement(String schemaName) {
        return StatementBuilder.dropSchemaStatement(schemaName);
    }

    public void executeDropSchema(String schemaName) {
        StatementExecutor.executeWrite(destinationConnection, getDropSchemaStatement(schemaName));
    }

    private String getCreateSchemaStatement(String schemaName) {
        return StatementBuilder.createSchemaStatement(schemaName);
    }

    public void executeCreateSchema(String schemaName) {
        StatementExecutor.executeWrite(destinationConnection, getCreateSchemaStatement(schemaName));
    }

    private String getDropTablesStatement(String schemaName, ArrayList<Table> tables) {
        StringBuilder builder = new StringBuilder();
        for (Table table : tables
        ) {
            builder.append(StatementBuilder.dropTableStatement(table, schemaName))
                    .append("\n");
        }
        return builder.toString();
    }

    public void executeDropTables(String schemaName, ArrayList<Table> tables) {
        StatementExecutor.executeWrite(destinationConnection, getDropTablesStatement(schemaName, tables));
    }

    public String getCreateTablesStatement(String schemaName, ArrayList<Table> tables) {
        StringBuilder builder = new StringBuilder();
        for (Table table : tables
        ) {
            builder.append(StatementBuilder.createTableStatement(table, schemaName))
                    .append("\n");
        }
        return builder.toString();
    }

    public void executeCreateTables(String schemaName, ArrayList<Table> tables) {
        StatementExecutor.executeWrite(destinationConnection, getCreateTablesStatement(schemaName, tables));
    }


    public String getCreateRelationsStatement(String schemaName, ArrayList<Table> tables) {
        StringBuilder builder = new StringBuilder();
        for (Table table : tables
        ) {
            builder.append(StatementBuilder.alterTableAddFkRelationStatement(table, schemaName))
                    .append("\n");
        }
        return builder.toString();
    }

    public void executeCreateRelations(String schemaName, ArrayList<Table> tables) {
        StatementExecutor.executeWrite(destinationConnection, getCreateRelationsStatement(schemaName, tables));
    }

    public String getCreateViewsStatement(String schemaName, ArrayList<View> views) {
        StringBuilder builder = new StringBuilder();
        for (View view : views
        ) {
            builder.append(StatementBuilder.createViewStatement(view, schemaName))
                    .append("\n");
        }
        return builder.toString();
    }

    public void executeCreateViews(String schemaName, ArrayList<View> views) {
        StatementExecutor.executeWrite(destinationConnection, getCreateViewsStatement(schemaName, views));
    }

}
