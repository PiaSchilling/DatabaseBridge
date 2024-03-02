package de.hdm_stuttgart.mi.di;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.connect.model.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.implementation.ConnectionHandlerImpl;
import de.hdm_stuttgart.mi.connect.model.ConnectionType;

import java.sql.DatabaseMetaData;

public class ConnectModule extends AbstractModule {

    final ConnectionDetails sourceConnectionDetails;
    final ConnectionDetails destinationConnectionDetails;

    final ConnectionHandlerImpl sourceConnectionHandler;

    final ConnectionHandlerImpl destinationConnectionHandler;

    public ConnectModule(ConnectionDetails sourceConnectionDetails, ConnectionDetails destinationConnectionDetails) {
        this.sourceConnectionDetails = sourceConnectionDetails;
        this.destinationConnectionDetails = destinationConnectionDetails;
        this.sourceConnectionHandler = new ConnectionHandlerImpl(ConnectionType.SOURCE, sourceConnectionDetails);
        this.destinationConnectionHandler = new ConnectionHandlerImpl(ConnectionType.DESTINATION, destinationConnectionDetails);
    }

    @Override
    protected void configure() {
        bind(DatabaseMetaData.class)
                .annotatedWith(Names.named("SourceDBMetaData"))
                .toInstance(sourceConnectionHandler.getDatabaseMetaData());
        bind(ConnectionHandler.class).annotatedWith(Names.named("sourceConnection")).toInstance(sourceConnectionHandler);
        bind(DatabaseMetaData.class)
                .annotatedWith(Names.named("DestinationDBMetaData"))
                .toInstance(destinationConnectionHandler.getDatabaseMetaData());
        bind(ConnectionHandler.class).annotatedWith(Names.named("destinationConnection")).toInstance(destinationConnectionHandler);

    }
}
