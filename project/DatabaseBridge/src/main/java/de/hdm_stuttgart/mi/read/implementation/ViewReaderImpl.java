package de.hdm_stuttgart.mi.read.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.read.model.View;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewReaderImpl implements de.hdm_stuttgart.mi.read.api.ViewReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final ConnectionHandler sourceConnection;
    private final DatabaseMetaData metaData;

    @Inject
    public ViewReaderImpl(@Named("sourceConnection") ConnectionHandler sourceConnection) {
        this.sourceConnection = sourceConnection;
        this.metaData = sourceConnection.getDatabaseMetaData();
    }

    @Override
    public ArrayList<View> readViews(String schemaName) {
        final ArrayList<View> views = new ArrayList<>();
        final ArrayList<String> viewNames = readViewNames(schemaName);

        for (String viewName : viewNames
        ) {
            views.add(new View(viewName, readCreateViewStatement(viewName,schemaName)));
        }

        return views;
    }

    private ArrayList<String> readViewNames(String schemaName) {
        ArrayList<String> views = new ArrayList<>();
        try (ResultSet tablesResult = metaData.getTables(schemaName, schemaName, null, new String[]{"VIEW"})) {
            while (tablesResult.next()) {
                String tableName = tablesResult.getString("TABLE_NAME");
                views.add(tableName);
            }
        } catch (SQLException sqlException) {
            log.log(Level.SEVERE, "SQLException while reading view names: " + sqlException.getMessage());
        }
        return views;
    }

    // TODO comment all and check all since i was tired
    private String readCreateViewStatement(String viewName, String schemaName) {
        String viewStatement = "";
        try (Statement statement = sourceConnection.getConnection().createStatement()) {
            final ResultSet viewResult = statement.executeQuery(buildViewStatementQuery(viewName, schemaName));
            while (viewResult.next()) {
                viewStatement = viewResult.getString(getViewStatementColumnName());
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while reading view statement: " + e.getMessage());
        }
        return viewStatement;
    }

    // TODO comment
    // TODO make consistent with userReader and evtl. extract to config file
    private String buildViewStatementQuery(String viewName, String schemaName) {
        return switch (sourceConnection.getConnectionDetails().getDatabaseSystem()) {
            case POSTGRES -> "select pg_get_viewdef('" + schemaName + "." + viewName + "', true)";
            case MYSQL, MARIADB -> "SHOW CREATE VIEW " + viewName;
        };
    }

    private String getViewStatementColumnName() {
        return switch (sourceConnection.getConnectionDetails().getDatabaseSystem()) {
            case POSTGRES -> "pg_get_viewdef";
            case MYSQL, MARIADB -> "Create View";
        };
    }
}
