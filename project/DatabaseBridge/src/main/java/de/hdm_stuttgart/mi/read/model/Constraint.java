package de.hdm_stuttgart.mi.read.model;

public class Constraint {

    private final ConstraintType constraintType;
    private final String value;

    /**
     * Model class for a single column constraint
     *
     * @param constraintType the type of this constraint
     * @param value          Additional value for this constraint e.g. default value when ConstraintType.DEFAULT
     */
    public Constraint(ConstraintType constraintType, String value) {
        this.constraintType = constraintType;
        this.value = value;
    }

    public Constraint(ConstraintType constraintType) {
        this.constraintType = constraintType;
        this.value = "";
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "constraintType=" + constraintType +
                ", value='" + value + '\'' +
                '}';
    }

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    public String getValue() {
        return value;
    }
}
