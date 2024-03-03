package de.hdm_stuttgart.mi.write.data.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.read.data.model.TableData;
import de.hdm_stuttgart.mi.util.StatementExecutor;
import de.hdm_stuttgart.mi.write.data.api.DataWriter;

import java.sql.Connection;
import java.util.ArrayList;


public class DataWriterImpl implements DataWriter {

    private final Connection destinationConnection;

    @Inject
    public DataWriterImpl(@Named("destinationConnection") ConnectionHandler destinationConnectionHandler) {
        this.destinationConnection = destinationConnectionHandler.getConnection();
    }

    @Override
    public void writeData(ArrayList<TableData> data) {
        for (TableData tableData : data) {
            ArrayList<String> statements = DataStatementBuilder.dataAsStatement(tableData);
            if (statements != null) {
                for (String statement : statements) {
                    StatementExecutor.executeWrite(destinationConnection, statement);
                }
            }

        }
    }
}
