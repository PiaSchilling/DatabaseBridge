package de.hdm_stuttgart.mi.di;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.implementation.ConnectionHandlerImpl;
import de.hdm_stuttgart.mi.connect.model.ConnectionType;
import de.hdm_stuttgart.mi.read.api.ColumnReader;
import de.hdm_stuttgart.mi.read.api.SchemaReader;
import de.hdm_stuttgart.mi.read.api.TableReader;
import de.hdm_stuttgart.mi.read.implementation.ColumnReaderImpl;
import de.hdm_stuttgart.mi.read.implementation.SchemaReaderImpl;
import de.hdm_stuttgart.mi.read.implementation.TableReaderImpl;

import java.sql.DatabaseMetaData;

public class BasicModule extends AbstractModule {

    final ConnectionDetails sourceConnectionDetails;
    final ConnectionDetails destinationConnectionDetails;

    public BasicModule(ConnectionDetails sourceConnectionDetails, ConnectionDetails destinationConnectionDetails) {
        this.sourceConnectionDetails = sourceConnectionDetails;
        this.destinationConnectionDetails = destinationConnectionDetails;
    }

    @Override
    protected void configure() {
        bind(ColumnReader.class).to(ColumnReaderImpl.class);
        bind(TableReader.class).to(TableReaderImpl.class);
        bind(SchemaReader.class).to(SchemaReaderImpl.class);
        bind(DatabaseMetaData.class)
                .annotatedWith(Names.named("SourceDBMetaData"))
                .toInstance(new ConnectionHandlerImpl(ConnectionType.SOURCE, sourceConnectionDetails)
                        .getDatabaseMetaData());
      /*  bind(DatabaseMetaData.class)
                .annotatedWith(Names.named("DestinationDBMetaData"))
                .toInstance(new ConnectionHandlerImplementation(ConnectionType.DESTINATION, destinationConnectionDetails)
                        .getDatabaseMetaData());*/ // TODO comment in when destination db should be used!

    }
}
