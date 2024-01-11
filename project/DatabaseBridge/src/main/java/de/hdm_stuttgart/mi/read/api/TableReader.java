package de.hdm_stuttgart.mi.read.api;

import de.hdm_stuttgart.mi.read.model.Table;

public interface TableReader {
    Table readTable(String tableName);
}
