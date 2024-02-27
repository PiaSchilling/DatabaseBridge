package de.hdm_stuttgart.mi.write.schema;

import de.hdm_stuttgart.mi.read.model.Schema;
import de.hdm_stuttgart.mi.read.model.Table;
import de.hdm_stuttgart.mi.read.model.View;

public class SchemaWriter {

    public void writeSchema(Schema schema) {
        System.out.println(StatementBuilder.dropSchemaStatement(schema));
        System.out.println(StatementBuilder.createSchemaStatement(schema));

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
