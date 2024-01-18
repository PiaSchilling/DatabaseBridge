package de.hdm_stuttgart.mi.read.api;

import de.hdm_stuttgart.mi.read.model.Schema;

public interface SchemaReader {
    Schema readSchema(String schemaName);
}
