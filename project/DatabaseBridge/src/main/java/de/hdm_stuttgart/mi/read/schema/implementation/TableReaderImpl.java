package de.hdm_stuttgart.mi.read.schema.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.read.schema.api.ColumnReader;
import de.hdm_stuttgart.mi.read.schema.api.TableReader;
import de.hdm_stuttgart.mi.read.schema.model.*;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    public Table readTable(String tableName, String schemaName) {
        final ArrayList<Column> columns = readTableColumns(tableName,schemaName);
        final ArrayList<FkRelation> importedFkRelations = readImportedFkRelations(tableName,schemaName);
        final ArrayList<FkRelation> exportedFkRelations = readExportedFkRelations(tableName,schemaName);

        // filter the columns to find all columns with a primary key constraint
        final ArrayList<Column> primaryKeys = columns
                .stream()
                .filter(column -> column.constraints()
                        .stream()
                        .anyMatch(constraint -> constraint.getConstraintType() == ConstraintType.PRIMARY_KEY))
                .collect(Collectors.toCollection(ArrayList::new));

        return new Table(tableName, columns, importedFkRelations, exportedFkRelations, primaryKeys);
    }

    private ArrayList<Column> readTableColumns(String tableName,String schemaName) {
        return columnReader.readTableColumns(tableName,schemaName);
    }


    /**
     * Read the foreign key relations where this table references other tables primary keys (this table is the child)
     *
     * @param tableName the name of the table of which the imported fk relations should be read
     * @return a list of the imported fk relations
     */
    private ArrayList<FkRelation> readImportedFkRelations(String tableName,String schemaName) {
        final ArrayList<FkRelation> importedFkRelations = new ArrayList<>();
        try {
            final ResultSet importedKeys = metaData.getImportedKeys(schemaName, schemaName, tableName);
            while (importedKeys.next()) {
                final String referencedTableName = importedKeys.getString("PKTABLE_NAME");
                final String referencedColumnName = importedKeys.getString("PKCOLUMN_NAME");
                final String referencingColumnName = importedKeys.getString("FKCOLUMN_NAME");
                final short updateRule = importedKeys.getShort("UPDATE_RULE");
                final short deleteRule = importedKeys.getShort("DELETE_RULE");
                final String fkName = importedKeys.getString("FK_NAME");


                importedFkRelations.add(new FkRelation(
                        referencedTableName,
                        referencedColumnName,
                        referencingColumnName,
                        fkName,
                        DeleteUpdateRule.fromCode(updateRule),
                        DeleteUpdateRule.fromCode(deleteRule)));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while reading readImportedFkRelations from table: " + tableName + e.getMessage());
            return importedFkRelations;
        }
        return importedFkRelations;
    }


    /**
     * Read the foreign key relations where this tables primary key is referenced by other tables (this table is the parent)
     *
     * @param tableName the name of the table of which the exported fk relations should be read
     * @return a list of the exported fk relations
     */
    private ArrayList<FkRelation> readExportedFkRelations(String tableName,String schemaName) {
        final ArrayList<FkRelation> exportedFkRelations = new ArrayList<>();
        try {
            final ResultSet importedKeys = metaData.getExportedKeys(schemaName, schemaName, tableName);
            while (importedKeys.next()) {
                final String referencingTableName = importedKeys.getString("FKTABLE_NAME");
                final String referencingColumnName = importedKeys.getString("FKCOLUMN_NAME");
                final String referencedColumnName = importedKeys.getString("PKCOLUMN_NAME");
                final short updateRule = importedKeys.getShort("UPDATE_RULE");
                final short deleteRule = importedKeys.getShort("DELETE_RULE");
                final String fkName = importedKeys.getString("FK_NAME");

                final String thisTableName = importedKeys.getString("FKTABLE_NAME");

                exportedFkRelations.add(new FkRelation(
                        referencingTableName,
                        referencingColumnName,
                        referencedColumnName,
                        fkName,
                        DeleteUpdateRule.fromCode(updateRule),
                        DeleteUpdateRule.fromCode(deleteRule)));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while reading readImportedFkRelations from table: " + tableName + e.getMessage());
            return exportedFkRelations;
        }
        return exportedFkRelations;
    }


}
