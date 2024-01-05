package de.hdm_stuttgart.mi.read.model;

import java.util.ArrayList;

public class Column {

    private final String name;

    private final int dataType;

    private final int maxLength;

    private final ArrayList<Constraint> constraints;


    /**
     * @param name        the name of this column
     * @param dataType    the type of data this column can hold, corresponds to a constant of java.sql.Types
     *                    (see <a href="https://docs.oracle.com/javase/8/docs/api/constant-values.html#java.sql.Types.ARRAY">...</a>)
     * @param maxLength   the maximum length of values this column can hold (e.g. for varchar)
     * @param constraints the constraints this column defines (e.g. not_null, unique, check,...)
     */
    public Column(String name, int dataType, int maxLength, ArrayList<Constraint> constraints) {
        this.name = name;
        this.dataType = dataType;
        this.maxLength = maxLength;
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", maxLength=" + maxLength +
                ", constraints=" + constraints +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getDataType() {
        return dataType;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }
}
