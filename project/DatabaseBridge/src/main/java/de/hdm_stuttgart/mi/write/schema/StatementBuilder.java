package de.hdm_stuttgart.mi.write.schema;

import de.hdm_stuttgart.mi.read.model.*;

import java.util.stream.Collectors;

public class StatementBuilder {

    /**
     * Get the CREATE TABLE statement for the provided {@code table}
     *
     * @param table the table for which the sql statement should be built
     * @return a SQL statement string which contains all attributes of this table
     * @example {@code CREATE TABLE departments(dept_no CHAR(4) NOT NULL UNIQUE,dept_name VARCHAR(40) NOT NULL UNIQUE,PRIMARY KEY (dept_no))}
     */
    public static String asCreateTableStatement(final Table table) {
        final String columnString = table.columns().stream().map(StatementBuilder::columnsAsStatement).collect(Collectors.joining());
        final String pkString = "PRIMARY KEY (" + String.join(",", table.primaryKeys().stream().map(Column::name).toList()) + ")";

        String createTableString = "CREATE TABLE " + table.name() + "(" + columnString + pkString;

        if (createTableString.endsWith(",")) {
            createTableString = createTableString.substring(0, createTableString.lastIndexOf(","));
        }

        return createTableString + ");";
    }

    /**
     * Get all ALTER TABLE ADD CONSTRAINT statement for the provided tables fk relations
     *
     * @param table the table for which the sql statement should be built
     * @return a SQL statement string which can be used to add all fk relations via ALTER TABLE
     * @example {@code ALTER TABLE titles ADD CONSTRAINT FOREIGN KEY(emp_no) REFERENCES employees(emp_no) ON UPDATE RESTRICT ON DELETE CASCADE;}
     */
    public static String alterTableAddFkRelationStatement(final Table table) {
        if (!table.isChildTable()) {
            return "";
        }

        return table.importedFkRelations()
                .stream()
                .map(StatementBuilder::fkRelationAsStatement)
                .map(e -> "ALTER TABLE " + table.name() + " ADD CONSTRAINT " + e + ";")
                .collect(Collectors.joining());
    }

    /**
     * Get a column as statement representation which can be used to build the columns in a CREATE TABLE statement
     * (primary key constraints will be ignored since they will be added on table level)
     *
     * @param column the column for which the sql statement should be built
     * @return a SQL statement string containing all attributes of this column
     * @example {@code first_name VARCHAR(14) NOT NULL}
     */
    private static String columnsAsStatement(final Column column) {
        final String constraintString = column.constraints().stream()
                .filter(constraint -> constraint.getConstraintType() != ConstraintType.PRIMARY_KEY)
                .map(StatementBuilder::constraintAsStatement)
                .collect(Collectors.joining(" "));

        return column.dataType().hasLength ? column.name() + " " + column.dataType() + "(" + column.maxLength() + ")" + " " + constraintString + "," :
                column.name() + " " + column.dataType() + " " + constraintString + ",";
    }

    /**
     * Get a constraint as statement representation which can be used to build the columns in a CREATE TABLE statement
     *
     * @param constraint the constraint for which the sql statement should be built
     * @return a SQL statement string
     * @example {@code DEFAULT false}
     */
    private static String constraintAsStatement(final Constraint constraint) {
        final ConstraintType constraintType = constraint.getConstraintType();
        return constraintType == ConstraintType.DEFAULT
                ? constraintType.asString + " " + constraint.getValue()
                : constraintType.asString;
    }

    /**
     * Get a fkRelation as statement representation which can be used to build the relations in a CREATE TABLE statement
     *
     * @param fkRelation the fkRelation for which the sql statement should be built
     * @return a SQL statement string containing all attributes of this fk relation
     * @example {@code REFERENCES employees(emp_no) ON UPDATE RESTRICT ON DELETE CASCADE}
     */
    private static String fkRelationAsStatement(final FkRelation fkRelation) {
        return "FOREIGN KEY(" + fkRelation.referencingColumnName() + ")"
                + " REFERENCES " + fkRelation.tableName() + "(" + fkRelation.referencedColumnName()
                + ") ON UPDATE " + fkRelation.updateRule().asString
                + " ON DELETE " + fkRelation.deleteRule().asString;
    }
}
