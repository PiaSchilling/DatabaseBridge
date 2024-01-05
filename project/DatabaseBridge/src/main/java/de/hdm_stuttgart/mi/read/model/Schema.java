package de.hdm_stuttgart.mi.read.model;

import java.util.ArrayList;

public class Schema {
    private final ArrayList<Table> tables;

    // TODO comment
    public Schema(ArrayList<Table> tables) {
        this.tables = tables;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }
}
