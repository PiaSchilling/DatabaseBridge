package de.hdm_stuttgart.mi.di;

import com.google.inject.AbstractModule;
import de.hdm_stuttgart.mi.write.schema.api.SchemaWriter;
import de.hdm_stuttgart.mi.write.schema.implementation.SchemaWriterImpl;

public class SchemaWriteModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SchemaWriter.class).to(SchemaWriterImpl.class);
    }
}
