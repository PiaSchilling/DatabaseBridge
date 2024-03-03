package de.hdm_stuttgart.mi.write.schema.api;

import de.hdm_stuttgart.mi.read.schema.model.Schema;

public interface SchemaWriter {

    /**
     * Get the DDL-Script capable of creating the schema in the SQL dialect specific to the {@code destinationConnection}
     * database system
     *
     * @param schema the schema for which the DDL-Script should be returned
     * @return an executable DDL-Script (mainly for the purpose of debugging, use {@code writeSchemaToDatabase} to write the
     * schema to the DB)
     */
    String getDDLScript(Schema schema);

    /**
     * Write the schema to the database which is defined in the {@code destinationConnection}-field of this class
     * @param schema the schema which should be written to the DB
     */
    void writeSchemaToDatabase(Schema schema);
}
