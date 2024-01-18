package de.hdm_stuttgart.mi.read.model;

import java.util.ArrayList;

public class Schema {
    private final ArrayList<Table> tables;
    private final ArrayList<Table> views;

    // TODO comment
    public Schema(ArrayList<Table> tables, ArrayList<Table> views) {
        this.tables = tables;
        this.views = views;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public ArrayList<Table> getViews() {
        return views;
    }
}
