package de.hdm_stuttgart.mi.read.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.read.model.View;
import de.hdm_stuttgart.mi.util.DbSysConstsProvider;

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
    private final DbSysConstsProvider constsProvider = DbSysConstsProvider.INSTANCE();

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
            views.add(new View(viewName, readCreateViewStatement(viewName, schemaName)));
        }

        return views;
    }

    /**
     * Read all view name available in one schema
     *
     * @param schemaName the name of the schema the views belong to
     * @return a list of view names
     */
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

    /**
     * Read the "createViewStatement" which was used to create the view
     *
     * @param viewName   the name of the view the createViewStatement should be read of
     * @param schemaName the name of the schema the view belongs to
     * @return a string containing the whole SQL-Query to recreate the view
     */
    private String readCreateViewStatement(String viewName, String schemaName) {
        String viewStatement = "";
        try (Statement statement = sourceConnection.getConnection().createStatement()) {
            final ResultSet viewResult = statement.executeQuery(buildSelectCreateViewStatementQuery(viewName, schemaName));
            while (viewResult.next()) {
                viewStatement = viewResult.getString(getViewStatementColumnName());
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while reading view statement: " + e.getMessage());
        }
        return viewStatement;
    }

    /**
     * Builds a database system specific query to select a "createViewStatement"
     *
     * @param viewName   the name of the view which should be selected
     * @param schemaName the name of the schema the view belongs to
     * @return a database system specific SQL-Query to select the "createViewStatement"
     */
    private String buildSelectCreateViewStatementQuery(String viewName, String schemaName) {
        return switch (sourceConnection.getConnectionDetails().getDatabaseSystem()) {
            case POSTGRES -> constsProvider.getPlaceholderProperty("viewStmtQueryPG", schemaName, viewName);
            case MYSQL, MARIADB -> constsProvider.getProperty("viewStmtQueryMSMD") + viewName;
        };
    }

    /**
     * Returns the database system specific column name which contains "createViewStatements"
     *
     * @return a String which contains the columns name
     */
    private String getViewStatementColumnName() {
        return switch (sourceConnection.getConnectionDetails().getDatabaseSystem()) {
            case POSTGRES -> constsProvider.getProperty("viewStmtColNamePG");
            case MYSQL, MARIADB -> constsProvider.getProperty("viewStmtColNameMSMD");
        };
    }
}
