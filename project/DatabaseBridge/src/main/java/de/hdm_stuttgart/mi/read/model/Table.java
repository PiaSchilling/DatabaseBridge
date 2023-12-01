package de.hdm_stuttgart.mi.read.model;

import java.util.ArrayList;

public class Table {
    final String name;
    ArrayList<Column> columns;

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, ArrayList<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", columns=" + columns +
                '}';
    }
}
