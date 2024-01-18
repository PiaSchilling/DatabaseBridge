package de.hdm_stuttgart.mi.di;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.implementation.ConnectionHandlerImpl;
import de.hdm_stuttgart.mi.connect.model.ConnectionType;
import de.hdm_stuttgart.mi.read.api.ColumnReader;
import de.hdm_stuttgart.mi.read.api.SchemaReader;
import de.hdm_stuttgart.mi.read.api.TableReader;
import de.hdm_stuttgart.mi.read.api.UsersReader;
import de.hdm_stuttgart.mi.read.implementation.ColumnReaderImpl;
import de.hdm_stuttgart.mi.read.implementation.SchemaReaderImpl;
import de.hdm_stuttgart.mi.read.implementation.TableReaderImpl;
import de.hdm_stuttgart.mi.read.implementation.UsersReaderImpl;

import java.sql.DatabaseMetaData;

public class BasicModule extends AbstractModule {

    final ConnectionDetails sourceConnectionDetails;
    final ConnectionDetails destinationConnectionDetails;

    final ConnectionHandlerImpl sourceConnectionHandler;

    final ConnectionHandlerImpl destinationConnectionHandler;

    public BasicModule(ConnectionDetails sourceConnectionDetails, ConnectionDetails destinationConnectionDetails) {
        this.sourceConnectionDetails = sourceConnectionDetails;
        this.destinationConnectionDetails = destinationConnectionDetails;
        this.sourceConnectionHandler = new ConnectionHandlerImpl(ConnectionType.SOURCE, sourceConnectionDetails);
        this.destinationConnectionHandler = new ConnectionHandlerImpl(ConnectionType.DESTINATION, destinationConnectionDetails);
    }

    @Override
    protected void configure() {
        bind(ColumnReader.class).to(ColumnReaderImpl.class);
        bind(TableReader.class).to(TableReaderImpl.class);
        bind(SchemaReader.class).to(SchemaReaderImpl.class);
        bind(UsersReader.class).to(UsersReaderImpl.class);
        bind(DatabaseMetaData.class)
                .annotatedWith(Names.named("SourceDBMetaData"))
                .toInstance(sourceConnectionHandler.getDatabaseMetaData());
        bind(ConnectionHandler.class).annotatedWith(Names.named("sourceConnection")).toInstance(sourceConnectionHandler);
      /*  bind(DatabaseMetaData.class)
                .annotatedWith(Names.named("DestinationDBMetaData"))
                .toInstance(destinationConnectionDetails.getDatabaseMetaData());*/ // TODO comment in when destination db should be used!

    }
}
