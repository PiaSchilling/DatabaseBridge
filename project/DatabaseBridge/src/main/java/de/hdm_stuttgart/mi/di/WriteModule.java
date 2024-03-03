package de.hdm_stuttgart.mi.di;

import com.google.inject.AbstractModule;
import de.hdm_stuttgart.mi.write.data.api.DataWriter;
import de.hdm_stuttgart.mi.write.data.implementation.DataWriterImpl;
import de.hdm_stuttgart.mi.write.schema.api.SchemaWriter;
import de.hdm_stuttgart.mi.write.schema.implementation.SchemaWriterImpl;

public class WriteModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SchemaWriter.class).to(SchemaWriterImpl.class);
        bind(DataWriter.class).to(DataWriterImpl.class);
    }
}
