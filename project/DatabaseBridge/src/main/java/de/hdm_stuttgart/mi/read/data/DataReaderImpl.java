package de.hdm_stuttgart.mi.read.data;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.read.data.DataReader;
import de.hdm_stuttgart.mi.read.schema.model.Column;
import de.hdm_stuttgart.mi.read.schema.model.Schema;
import de.hdm_stuttgart.mi.read.schema.model.Table;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataReaderImpl implements DataReader {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final ConnectionHandler connectionHandler;

    @Inject
    public DataReaderImpl(@Named("sourceConnection")ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    private CachedRowSet readTableData(String tableName) {
        try{
            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            String schema = connectionHandler.getConnectionDetails().getSchema();
            try (PreparedStatement preparedStatement = connectionHandler.getConnection().prepareStatement("SELECT * FROM " + schema + '.' + tableName)) {
                rowSet.populate(preparedStatement.executeQuery());

            }
            return rowSet;
        }catch (SQLException sqlException) {
            log.log(Level.SEVERE, "SQLException while reading table data: " + sqlException.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<TableData> readData(Schema schema) {
        ArrayList<TableData> data = new ArrayList<>();
        for(Table table: schema.tables()) {
            ArrayList<String> columns = new ArrayList<>();
            for(Column column : table.columns()) {
                columns.add(column.name());
            }
            data.add(new TableData(schema.name(), table.name(), columns, readTableData(table.name())));
        }

        return data;
    }

}
