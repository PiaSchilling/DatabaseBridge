package de.hdm_stuttgart.mi.read.schema.model;

import de.hdm_stuttgart.mi.util.SQLType;

import java.util.ArrayList;

/**
 * Model class for a single table column
 *
 * @param name        the name of this column
 * @param dataType    the type of data this column can hold, corresponds to a constant of java.sql.Types
 *                    (see <a href="https://docs.oracle.com/javase/8/docs/api/constant-values.html#java.sql.Types.ARRAY">...</a>)
 * @param maxLength   the maximum length of values this column can hold (e.g. for varchar)
 * @param constraints the constraints this column defines (e.g. not_null, unique, check,...)
 */
public record Column(String name, SQLType dataType, int maxLength, ArrayList<Constraint> constraints) {
}
