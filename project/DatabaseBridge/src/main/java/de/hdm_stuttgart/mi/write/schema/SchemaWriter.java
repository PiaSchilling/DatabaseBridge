package de.hdm_stuttgart.mi.write.schema;

import de.hdm_stuttgart.mi.read.schema.model.Schema;
import de.hdm_stuttgart.mi.read.schema.model.Table;
import de.hdm_stuttgart.mi.read.schema.model.View;

import java.util.ArrayList;

public class SchemaWriter {

    public void writeSchemaTest(Schema schema) {
        final String schemaName = schema.name();
        final ArrayList<Table> tables = schema.tables();
        System.out.println(writeDropSchema(schemaName));
        System.out.println(writeCreateSchema(schemaName));
        System.out.println(writeDropTables(schemaName, tables));
        System.out.println(writeCreateTables(schemaName, tables));
        System.out.println(writeRelations(schemaName, tables));
        System.out.println(writeViews(schemaName, schema.views()));
    }

    public String writeDropSchema(String schemaName) {
        return StatementBuilder.dropSchemaStatement(schemaName);
    }

    public String writeCreateSchema(String schemaName) {
        return StatementBuilder.createSchemaStatement(schemaName);
    }

    public String writeDropTables(String schemaName, ArrayList<Table> tables) {
        StringBuilder builder = new StringBuilder();
        for (Table table : tables
        ) {
            builder.append(StatementBuilder.dropTableStatement(table, schemaName))
                    .append("\n");
        }
        return builder.toString();
    }

    public String writeCreateTables(String schemaName, ArrayList<Table> tables) {
        StringBuilder builder = new StringBuilder();
        for (Table table : tables
        ) {
            builder.append(StatementBuilder.createTableStatement(table, schemaName))
                    .append("\n");
        }
        return builder.toString();
    }

    public String writeRelations(String schemaName, ArrayList<Table> tables) {
        StringBuilder builder = new StringBuilder();
        for (Table table : tables
        ) {
            builder.append(StatementBuilder.alterTableAddFkRelationStatement(table, schemaName))
                    .append("\n");
        }
        return builder.toString();
    }

    public String writeViews(String schemaName, ArrayList<View> views) {
        StringBuilder builder = new StringBuilder();
        for (View view : views
        ) {
            builder.append(StatementBuilder.createViewStatement(view, schemaName))
                    .append("\n");
        }
        return builder.toString();
    }

}
