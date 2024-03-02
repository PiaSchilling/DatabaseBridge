package de.hdm_stuttgart.mi.write.schema.api;

import de.hdm_stuttgart.mi.read.schema.model.Schema;

public interface SchemaWriter {
    // TODO comment
    String getDDLScript(Schema schema);
    void executeDDLScript(Schema schema);
}
