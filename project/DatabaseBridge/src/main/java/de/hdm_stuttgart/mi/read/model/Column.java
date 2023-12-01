package de.hdm_stuttgart.mi.read.model;

public class Column {

    final String name;

    public Column(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                '}';
    }
}
