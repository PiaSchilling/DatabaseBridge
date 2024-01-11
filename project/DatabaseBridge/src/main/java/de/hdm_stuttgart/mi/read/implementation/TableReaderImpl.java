package de.hdm_stuttgart.mi.read.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.read.api.ColumnReader;
import de.hdm_stuttgart.mi.read.api.TableReader;
import de.hdm_stuttgart.mi.read.model.Column;
import de.hdm_stuttgart.mi.read.model.FkRelation;
import de.hdm_stuttgart.mi.read.model.Table;

import java.sql.DatabaseMetaData;
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
        final ArrayList<FkRelation> importedFkRelations = readImportedFkRelations();
        final ArrayList<FkRelation> exportedFkRelations = readExportedFkRelations();

        return new Table(tableName, columns, importedFkRelations, exportedFkRelations);
    }

    private ArrayList<Column> readTableColumns(String tableName) {
        return columnReader.readTableColumns(tableName);
    }

    private ArrayList<FkRelation> readImportedFkRelations() {
        // TODO implement me
        return new ArrayList<>();
    }

    private ArrayList<FkRelation> readExportedFkRelations() {
        // TODO implement me
        return new ArrayList<>();
    }


}
