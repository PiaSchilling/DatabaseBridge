package de.hdm_stuttgart.mi.read.api;

import de.hdm_stuttgart.mi.read.model.Column;

import java.util.ArrayList;

public interface ColumnReader {
    ArrayList<Column> readTableColumns(String tableName);
}
