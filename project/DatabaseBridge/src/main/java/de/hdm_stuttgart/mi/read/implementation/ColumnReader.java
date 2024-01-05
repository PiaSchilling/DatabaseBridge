package de.hdm_stuttgart.mi.read.implementation;

import de.hdm_stuttgart.mi.read.model.Column;
import de.hdm_stuttgart.mi.read.model.Constraint;
import de.hdm_stuttgart.mi.read.model.ConstraintType;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColumnReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final DatabaseMetaData metaData;

    public ColumnReader(DatabaseMetaData metaData) {
        this.metaData = metaData;
    }

    public ArrayList<Column> readTableColumns(String tableName) {
        ArrayList<Column> columns = new ArrayList<>();
        try {

            final ResultSet currentColumn = metaData.getColumns(null, null, tableName, null);
            while (currentColumn.next()) {
                String columnName = currentColumn.getString("COLUMN_NAME");
                int columnDataType = currentColumn.getInt("DATA_TYPE");
                final ArrayList<Constraint> constraints = checkConstraints(currentColumn, columnName,tableName);
                columns.add(new Column(columnName, columnDataType, 1, constraints)); // TODO add length
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while reading columns from table " + tableName + ": " + e.getMessage());
            return null;
        }

        return columns;
    }

    private ArrayList<Constraint> checkConstraints(ResultSet column, String columnName, String tableName) {
        final ArrayList<Constraint> constraints = new ArrayList<>();

        constraints.add(checkNotNullConstraint(column));
        constraints.add(checkUniqueConstraint(columnName, tableName));
        constraints.add(checkPrimaryKeyConstraint(columnName, tableName));
        constraints.add(checkForeignKeyConstraint(columnName, tableName));
        constraints.add(checkDetaultConstraint(column));
       // constraints.add(checkCheckConstraint(column)); // TODO fix

        constraints.removeIf(Objects::isNull);

        return constraints;
    }

    /**
     * Checks if {@code column} defines the NOT_NULL constraint
     *
     * @param column the column to check
     * @return a Constraint object with type NOT_NULL if the column defines this the constraint, null if not or if an error
     * occurs
     */
    private Constraint checkNotNullConstraint(ResultSet column) {
        // TODO test
        try {
            String isNullable = column.getString("IS_NULLABLE");
            if (Objects.equals(isNullable, "NO")) {
                return new Constraint(ConstraintType.NOT_NULL);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while extracting IS_NULLABLE from column: " + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * Checks if {@code column} defines the UNIQUE constraint
     *
     * @param columnName the name of the column to check
     * @return a Constraint object with type UNIQUE if the column defines this the constraint, null if not or if an error
     * occurs
     */
    private Constraint checkUniqueConstraint(String columnName, String tableName) {

        // TODO test
        try {
            ResultSet indexes = metaData.getIndexInfo(null, null, tableName, false, true);
            while (indexes.next()) {

                String currentColumnName = indexes.getString("COLUMN_NAME");
                boolean isUnique = !indexes.getBoolean("NON_UNIQUE");

                if (Objects.equals(currentColumnName, columnName) && isUnique) {
                    return new Constraint(ConstraintType.UNIQUE);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while extracting NON_UNIQUE from column: " + e.getMessage());
            return null;
        }
        return null;
    }
    // TODO check how to reduce duplicate code

    private Constraint checkPrimaryKeyConstraint(String columnName, String tableName) {
        // TODO test

        try {
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
            while (primaryKeys.next()) {
                String currentColumnName = primaryKeys.getString("COLUMN_NAME");
                if (Objects.equals(currentColumnName, columnName)) {
                    // TODO test for composite primary keys
                    return new Constraint(ConstraintType.PRIMARY_KEY);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while extracting PRIMARY_KEY from column: " + e.getMessage());
            return null;
        }
        return null;
    }

    private Constraint checkForeignKeyConstraint(String columnName, String tableName) {
        // TODO test

        try {
            ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableName);

            while (foreignKeys.next()) {
                String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
                String pkTableName = foreignKeys.getString("PKTABLE_NAME");
                String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");

                if (Objects.equals(fkColumnName, columnName)) {
                    return new Constraint(ConstraintType.FOREIGN_KEY, pkTableName + "," + pkColumnName);
                }
            }

        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while extracting FOREIGN_KEY from column: " + e.getMessage());
            return null;
        }
        return null;
    }

    private Constraint checkDetaultConstraint(ResultSet colum) {
        // TODO test

        try {
            String defaultValue = colum.getString("COLUMN_DEF");

            if (defaultValue != null) {
                return new Constraint(ConstraintType.DEFAULT, defaultValue);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while extracting DEFAULT from column: " + e.getMessage());
            return null;
        }
        return null;
    }

    private Constraint checkCheckConstraint(ResultSet column) {
        // TODO find a way to extract check constraints

        try {
            String checkExpression = column.getString("CHECK");

            if (checkExpression != null) {
                return new Constraint(ConstraintType.CHECK, checkExpression);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while extracting CHECK from column: " + e.getMessage());
            return null;
        }
        return null;
    }
}
