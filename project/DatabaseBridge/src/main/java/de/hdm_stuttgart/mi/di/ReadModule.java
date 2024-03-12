package de.hdm_stuttgart.mi.di;

import com.google.inject.AbstractModule;
import de.hdm_stuttgart.mi.read.data.api.DataReader;
import de.hdm_stuttgart.mi.read.data.implementation.DataReaderImpl;
import de.hdm_stuttgart.mi.read.schema.api.*;
import de.hdm_stuttgart.mi.read.schema.implementation.*;

public class ReadModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ColumnReader.class).to(ColumnReaderImpl.class);
        bind(TableReader.class).to(TableReaderImpl.class);
        bind(ViewReader.class).to(ViewReaderImpl.class);
        bind(SchemaReader.class).to(SchemaReaderImpl.class);
        bind(UsersReader.class).to(UsersReaderImpl.class);
        bind(PrivilegeReader.class).to(PrivilegeReaderImpl.class);
        bind(DataReader.class).to(DataReaderImpl.class);
    }
}
