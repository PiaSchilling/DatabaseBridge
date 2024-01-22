package de.hdm_stuttgart.mi.read.model;

import de.hdm_stuttgart.mi.util.SQLType;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

    /**
     * Get this column as statement representation which can be used to build the columns in a CREATE TABLE statement
     *
     * @return a SQL statement string containing all attributes of this column
     * @example {@code first_name VARCHAR(14) NOT NULL}
     */
    public String asStatement() {
        final String constraintString = constraints.stream()
                .map(Constraint::asStatement)
                .collect(Collectors.joining(" "));

        return dataType.hasLength ? name + " " + dataType + "(" + maxLength + ")" + " " + constraintString + "," :
                name + " " + dataType + " " + constraintString + ",";
    }
}
