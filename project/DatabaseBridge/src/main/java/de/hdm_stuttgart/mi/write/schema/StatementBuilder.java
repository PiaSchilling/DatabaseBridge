package de.hdm_stuttgart.mi.write.schema;

import de.hdm_stuttgart.mi.read.model.*;
import de.hdm_stuttgart.mi.util.Consts;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class StatementBuilder {

    /**
     * Get the CREATE TABLE statement for the provided {@code table}
     *
     * @param table the table for which the sql statement should be built
     * @return a SQL statement string which contains all attributes of this table
     * @example {@code CREATE TABLE departments(dept_no CHAR(4) NOT NULL UNIQUE,dept_name VARCHAR(40) NOT NULL UNIQUE,PRIMARY KEY (dept_no))}
     */
    public static String createTableStatement(final Table table) {
        final String columnString = table.columns().stream().map(StatementBuilder::columnsAsStatement).collect(Collectors.joining());

        final ArrayList<Column> primaryKeys = table.primaryKeys();
        final String pkString = primaryKeys.isEmpty()
                ? ""
                : "PRIMARY KEY (" + String.join(",", primaryKeys.stream().map(Column::name).toList()) + ")";

        String createTableString = "CREATE TABLE " + table.name() + "(" + columnString + pkString;

        if (createTableString.endsWith(",")) {
            createTableString = createTableString.substring(0, createTableString.lastIndexOf(","));
        }

        return createTableString + ");";
    }

    public static String createViewStatement(final View view) {
        return "CREATE OR REPLACE VIEW " + view.name() + " AS" + view.createViewStatement();
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

        // special case: auto increment (resp. serial) columns in postgres may not define the datatype and default constraint
        if(constraintString.contains("SERIAL")){
            return column.name() + " " + constraintString.replaceAll("DEFAULT.*", "") + ",";
        }
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

        return switch (constraintType) {
            case NOT_NULL, UNIQUE, PRIMARY_KEY, FOREIGN_KEY -> constraintType.asString;
            case DEFAULT -> constraintType.asString + " " + constraint.getValue();
            case AUTO_INKREMENT -> Consts.autoIncrementConstraintName;
        };
    }

    /**
     * Get a fkRelation as statement representation which can be used to build the relations in a CREATE TABLE statement
     *
     * @param fkRelation the fkRelation for which the sql statement should be built
     * @return a SQL statement string containing all attributes of this fk relation
     * @example {@code REFERENCES employees(emp_no) ON UPDATE RESTRICT ON DELETE CASCADE}
     */
    private static String fkRelationAsStatement(final FkRelation fkRelation) {
        return fkRelation.fkName() + " FOREIGN KEY(" + fkRelation.referencingColumnName() + ")"
                + " REFERENCES " + fkRelation.tableName() + "(" + fkRelation.referencedColumnName()
                + ") ON UPDATE " + fkRelation.updateRule().asString
                + " ON DELETE " + fkRelation.deleteRule().asString;
    }

    /**
     * Create a INSERT statement from a cashedRowSet to insert data to a new table
     *
     */
    public static ArrayList<String> dataAsStatement(final Table table) {
        if(table.data() == null || table.data().size() <= 1) {
            return null;
        }

        ArrayList<String> statements = new ArrayList<>();
        StringBuilder base = new StringBuilder("INSERT INTO ")
                .append(table.name())
                .append(" (")
                .append(String.join(", ", table.columns().stream().map(Column::name).toArray(String[]::new)))
                .append(") VALUES ");
            //Get values of all rows in Form (value1, value2, ...)
            try {
                int rowCount = 0;
                StringBuilder insertQuery = base;
                while (table.data().next()) {
                    if(rowCount % 100 == 0 && rowCount > 0) {
                        insertQuery.setLength(insertQuery.length() - 2);
                        insertQuery.append(";");
                        statements.add(insertQuery.toString());
                        insertQuery.setLength(0);
                        insertQuery = base;
                    }
                    StringBuilder values = new StringBuilder("(");
                    for (int i = 1; i <= table.columns().size(); i++) {
                        Object value = table.data().getObject(i);
                        values.append(getFormattedValue(value)).append(", ");
                    }
                    values.setLength(values.length() - 2);
                    values.append("), ");
                    insertQuery.append(values);
                    rowCount++;
                }
                insertQuery.setLength(insertQuery.length() - 2);
                insertQuery.append(";");
                statements.add(insertQuery.toString());
                table.data().close();
                return statements;
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
                return null;
            }
    }

    private static String getFormattedValue(Object value) {
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        } else {
            return (value == null) ? "NULL" : "'" + value + "'";
        }
    }
}


/*

public static ArrayList<String> dataAsStatement(final Table table) {}
        int row = 0;
        ArrayList<String> statements = new ArrayList<String>();
        if(table.data() == null || table.data().size() <= 1) {
            return null;
        }
            StringBuilder insertQueryBuilder = new StringBuilder("INSERT INTO ")
                    .append(table.name())
                    .append(" (")
                    .append(String.join(", ", table.columns().stream().map(Column::name).toArray(String[]::new)))
                    .append(") VALUES ");
            //Get values of all rows in Form (value1, value2, ...)
            try {
                while (table.data().next()) {

                    StringBuilder values = new StringBuilder("(");
                    for (int i = 1; i <= table.columns().size(); i++) {
                        Object value = table.data().getObject(i);
                        values.append(getFormattedValue(value)).append(", ");
                    }
                    values.setLength(values.length() - 2);
                    values.append("), ");
                    insertQueryBuilder.append(values);
                }
                insertQueryBuilder.setLength(insertQueryBuilder.length() - 2);
                insertQueryBuilder.append(";");
                table.data().close();
                return insertQueryBuilder.toString();
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
                return null;
            }
    }
  }
 */
