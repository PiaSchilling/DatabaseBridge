package de.hdm_stuttgart.mi.write.schema;

import de.hdm_stuttgart.mi.read.model.Schema;
import de.hdm_stuttgart.mi.read.model.Table;

public class SchemaWriter {

    public void writeSchema(Schema schema){

        for (Table table : schema.tables()
                ) {
            System.out.println(StatementBuilder.asCreateTableStatement(table));
        }

        for (Table table : schema.tables()
        ) {
            System.out.println(StatementBuilder.alterTableAddFkRelationStatement(table));
        }
    }
}
