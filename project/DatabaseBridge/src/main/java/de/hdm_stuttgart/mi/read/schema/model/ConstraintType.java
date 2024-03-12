package de.hdm_stuttgart.mi.read.schema.model;

/**
 * Column constraint types
 */
public enum ConstraintType {
    NOT_NULL("NOT NULL"),
    UNIQUE("UNIQUE"),
    PRIMARY_KEY("PRIMARY KEY"),
    FOREIGN_KEY("FOREIGN KEY"),
    DEFAULT("DEFAULT"),
    AUTO_INKREMENT("AUTO_INCREMENT");

    public final String asString;

    ConstraintType(String asString) {
        this.asString = asString;
    }
}
