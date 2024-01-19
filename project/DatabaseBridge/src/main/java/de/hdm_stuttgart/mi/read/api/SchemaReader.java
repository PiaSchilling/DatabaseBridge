package de.hdm_stuttgart.mi.read.api;

import de.hdm_stuttgart.mi.read.model.Schema;

public interface SchemaReader {
    /**
     * Read a whole database schema
     * @param schemaName the name of the schema which should be read
     * @return a schema object containing tables, users, views and privileges
     */
    Schema readSchema(String schemaName);
}
