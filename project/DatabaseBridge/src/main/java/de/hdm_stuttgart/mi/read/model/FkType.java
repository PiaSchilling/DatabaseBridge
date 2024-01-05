package de.hdm_stuttgart.mi.read.model;

public enum FkType {

    /**
     * Primary key columns that are referenced by the given table's foreign key columns (the primary keys imported by a table).
     */
    IMPORTED,
    /**
     * Foreign key columns that reference the given table's primary key columns (the foreign keys exported by a table).
     */
    EXPORTED
}
