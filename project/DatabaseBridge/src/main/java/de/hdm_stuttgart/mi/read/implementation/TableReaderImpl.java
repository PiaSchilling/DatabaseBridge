package de.hdm_stuttgart.mi.read.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.read.api.ColumnReader;
import de.hdm_stuttgart.mi.read.api.TableReader;
import de.hdm_stuttgart.mi.read.model.Column;
import de.hdm_stuttgart.mi.read.model.DeleteUpdateRule;
import de.hdm_stuttgart.mi.read.model.FkRelation;
import de.hdm_stuttgart.mi.read.model.Table;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class TableReaderImpl implements TableReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final DatabaseMetaData metaData;

    private final ColumnReader columnReader;

    @Inject
    public TableReaderImpl(@Named("SourceDBMetaData") DatabaseMetaData metaData, ColumnReader columnReader) {
        this.metaData = metaData;
        this.columnReader = columnReader;
    }

    @Override
    public Table readTable(String tableName) {
        final ArrayList<Column> columns = readTableColumns(tableName);
        final ArrayList<FkRelation> importedFkRelations = readImportedFkRelations(tableName);
        final ArrayList<FkRelation> exportedFkRelations = readExportedFkRelations(tableName);

        return new Table(tableName, columns, importedFkRelations, exportedFkRelations);
    }

    private ArrayList<Column> readTableColumns(String tableName) {
        return columnReader.readTableColumns(tableName);
    }


    /**
     * Read the foreign key relations where this table references other tables primary keys (this table is the child)
     *
     * @param tableName the name of the table of which the imported fk relations should be read
     * @return a list of the imported fk relations
     */
    private ArrayList<FkRelation> readImportedFkRelations(String tableName) {
        final ArrayList<FkRelation> importedFkRelations = new ArrayList<>();
        try {
            final ResultSet importedKeys = metaData.getImportedKeys(null, null, tableName);
            while (importedKeys.next()) {
                final String referencedTableName = importedKeys.getString("PKTABLE_NAME");
                final String referencedColumnName = importedKeys.getString("PKCOLUMN_NAME");
                final short updateRule = importedKeys.getShort("UPDATE_RULE");
                final short deleteRule = importedKeys.getShort("DELETE_RULE");
                final String fkName = importedKeys.getString("FK_NAME");


                importedFkRelations.add(new FkRelation(
                        referencedTableName,
                        referencedColumnName,
                        fkName,
                        DeleteUpdateRule.fromCode(updateRule),
                        DeleteUpdateRule.fromCode(deleteRule)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return importedFkRelations;
    }


    /**
     * Read the foreign key relations where this tables primary key is referenced by other tables (this table is the parent)
     *
     * @param tableName the name of the table of which the exported fk relations should be read
     * @return a list of the exported fk relations
     */
    private ArrayList<FkRelation> readExportedFkRelations(String tableName) {
        final ArrayList<FkRelation> importedFkRelations = new ArrayList<>();
        try {
            final ResultSet importedKeys = metaData.getExportedKeys(null, null, tableName);
            while (importedKeys.next()) {
                final String referencingTableName = importedKeys.getString("FKTABLE_NAME");
                final String referencingColumnName = importedKeys.getString("FKCOLUMN_NAME");
                final short updateRule = importedKeys.getShort("UPDATE_RULE");
                final short deleteRule = importedKeys.getShort("DELETE_RULE");
                final String fkName = importedKeys.getString("FK_NAME");

                final String thisTableName = importedKeys.getString("FKTABLE_NAME");

                importedFkRelations.add(new FkRelation(
                        referencingTableName,
                        referencingColumnName,
                        fkName,
                        DeleteUpdateRule.fromCode(updateRule),
                        DeleteUpdateRule.fromCode(deleteRule)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return importedFkRelations;
    }


}
