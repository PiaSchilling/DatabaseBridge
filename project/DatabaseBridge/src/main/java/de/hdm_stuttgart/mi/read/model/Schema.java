package de.hdm_stuttgart.mi.read.model;

import java.util.ArrayList;

public class Schema {
    final ArrayList<Table> tables;

    public Schema(ArrayList<Table> tables) {
        this.tables = tables;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }
}
