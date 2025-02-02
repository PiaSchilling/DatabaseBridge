package de.hdm_stuttgart.mi.read.schema.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.read.schema.api.ColumnReader;
import de.hdm_stuttgart.mi.read.schema.model.Column;
import de.hdm_stuttgart.mi.read.schema.model.Constraint;
import de.hdm_stuttgart.mi.read.schema.model.ConstraintType;
import de.hdm_stuttgart.mi.util.consts.SourceConsts;
import de.hdm_stuttgart.mi.util.SQLType;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColumnReaderImpl implements ColumnReader {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final DatabaseMetaData metaData;

    @Inject
    public ColumnReaderImpl(@Named("SourceDBMetaData") DatabaseMetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public ArrayList<Column> readTableColumns(String tableName,String schemaName) {
        ArrayList<Column> columns = new ArrayList<>();
        try {

            final ResultSet metaColumns = metaData.getColumns(schemaName, schemaName, tableName, null);
            while (metaColumns.next()) {
                String columnName = metaColumns.getString("COLUMN_NAME");
                int columnDataTypeCode = metaColumns.getInt("DATA_TYPE");
                int columnSize = metaColumns.getInt("COLUMN_SIZE");
                // Map varchar with exceeding max length to TEXT type
                if(columnDataTypeCode == Types.VARCHAR && columnSize > SourceConsts.varcharMaxLength){
                    columnDataTypeCode = SQLType.TEXT.getTypeCode();
                }
                final ArrayList<Constraint> constraints = readConstraints(metaColumns, columnName, tableName);
                columns.add(new Column(columnName, SQLType.fromTypeCode(columnDataTypeCode), columnSize, constraints));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while reading columns from table " + tableName + ": " + e.getMessage());
            return null;
        }

        return columns;
    }

    /**
     * Read all constraints of one table columns
     *
     * @param column     the column the constraints should be read of
     * @param columnName the name of the column the constraints should be read of
     * @param tableName  the name of the table the column belongs to
     * @return a list of constraints, containing only constraints which are defined by the column
     */
    private ArrayList<Constraint> readConstraints(ResultSet column, String columnName, String tableName) {
        final ArrayList<Constraint> constraints = new ArrayList<>();

        constraints.add(readAutoInkrementConstraint(column));
        constraints.add(readNotNullConstraint(column));
        constraints.add(readUniqueConstraint(columnName, tableName));
        constraints.add(readPrimaryKeyConstraint(columnName, tableName));
        constraints.add(readDefaultConstraint(column));

        constraints.removeIf(Objects::isNull);

        return constraints;
    }

    /**
     * Checks if {@code column} this column is auto incremented
     *
     * @param column the column to check
     * @return a Constraint object with type AUTO_INKREMENT if the column defines this the constraint, null if not or if an error
     * occurs
     */
    private Constraint readAutoInkrementConstraint(ResultSet column) {
        try {
            String isAutoIncrement = column.getString("IS_AUTOINCREMENT");
            if (Objects.equals(isAutoIncrement, "YES")) {
                return new Constraint(ConstraintType.AUTO_INKREMENT);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while extracting IS_AUTOINCREMENT from column: " + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * Checks if {@code column} defines the NOT_NULL constraint
     *
     * @param column the column to check
     * @return a Constraint object with type NOT_NULL if the column defines this the constraint, null if not or if an error
     * occurs
     */
    private Constraint readNotNullConstraint(ResultSet column) {
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
    private Constraint readUniqueConstraint(String columnName, String tableName) {
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

    /**
     * Checks if {@code column} defines a PRIMARY KEY constraint
     *
     * @param columnName the name of the column to check
     * @param tableName  the name of the table to check
     * @return a Constraint object with type PRIMARY KEY if the column defines this the constraint, null if not or if an error
     * occurs
     */
    private Constraint readPrimaryKeyConstraint(String columnName, String tableName) {
        try {
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
            while (primaryKeys.next()) {
                String currentColumnName = primaryKeys.getString("COLUMN_NAME");
                if (Objects.equals(currentColumnName, columnName)) {
                    return new Constraint(ConstraintType.PRIMARY_KEY);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while extracting PRIMARY_KEY from column: " + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * Checks if {@code column} defines the DEFAULT constraint
     *
     * @return a Constraint object with type DEFAULT and the according default value if the column defines this the
     * constraint, null if not or if an error occurs
     */
    private Constraint readDefaultConstraint(ResultSet column) {
        try {
            String defaultValue = column.getString("COLUMN_DEF");

            if (defaultValue != null) {
                return new Constraint(ConstraintType.DEFAULT, defaultValue);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while extracting DEFAULT from column: " + e.getMessage());
            return null;
        }
        return null;
    }
}
