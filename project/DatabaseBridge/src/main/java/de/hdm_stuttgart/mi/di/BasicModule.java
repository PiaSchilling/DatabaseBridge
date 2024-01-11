package de.hdm_stuttgart.mi.di;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.hdm_stuttgart.mi.connect.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.ConnectionHandlerImplementation;
import de.hdm_stuttgart.mi.connect.ConnectionType;
import de.hdm_stuttgart.mi.read.api.ColumnReader;
import de.hdm_stuttgart.mi.read.api.SchemaReader;
import de.hdm_stuttgart.mi.read.api.TableReader;
import de.hdm_stuttgart.mi.read.implementation.ColumnReaderImplementation;
import de.hdm_stuttgart.mi.read.implementation.SchemaReaderImplementation;
import de.hdm_stuttgart.mi.read.implementation.TableReaderImplementation;

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
        bind(ColumnReader.class).to(ColumnReaderImplementation.class);
        bind(TableReader.class).to(TableReaderImplementation.class);
        bind(SchemaReader.class).to(SchemaReaderImplementation.class);
        bind(DatabaseMetaData.class)
                .annotatedWith(Names.named("SourceDBMetaData"))
                .toInstance(new ConnectionHandlerImplementation(ConnectionType.SOURCE, sourceConnectionDetails)
                        .getDatabaseMetaData()); // TODO clean up and use factory
      /*  bind(DatabaseMetaData.class)
                .annotatedWith(Names.named("DestinationDBMetaData"))
                .toInstance(new ConnectionHandlerImplementation(ConnectionType.DESTINATION, destinationConnectionDetails)
                        .getDatabaseMetaData());*/ // TODO comment in when destination db should be used!

    }
}
