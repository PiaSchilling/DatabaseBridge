package de.hdm_stuttgart.mi.write.schema;

import de.hdm_stuttgart.mi.read.schema.model.Schema;
import de.hdm_stuttgart.mi.read.schema.model.Table;
import de.hdm_stuttgart.mi.read.schema.model.View;

public class SchemaWriter {

    public void writeSchema(Schema schema) {
        System.out.println(StatementBuilder.dropSchemaStatement(schema.name()));
        System.out.println(StatementBuilder.createSchemaStatement(schema.name()));

        for (Table table : schema.tables()
        ) {
            System.out.println(StatementBuilder.dropTableStatement(table, schema.name()));
        }

        for (Table table : schema.tables()
        ) {
            System.out.println(StatementBuilder.createTableStatement(table, schema.name()));
        }

        for (Table table : schema.tables()
        ) {
            System.out.println(StatementBuilder.alterTableAddFkRelationStatement(table, schema.name()));
        }

        for (View view : schema.views()) {
            System.out.println(StatementBuilder.createViewStatement(view, schema.name()));
        }
    }
}
