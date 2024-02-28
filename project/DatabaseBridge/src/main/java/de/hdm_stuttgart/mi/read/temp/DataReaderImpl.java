package de.hdm_stuttgart.mi.read.temp;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.read.temp.DataReader;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataReaderImpl implements DataReader {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final ConnectionHandler connectionHandler;

    @Inject
    public DataReaderImpl(@Named("sourceConnection")ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    @Override
    public CachedRowSet readTableData(String tableName) {
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

}
