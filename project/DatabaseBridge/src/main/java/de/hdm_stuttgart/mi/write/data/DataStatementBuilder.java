package de.hdm_stuttgart.mi.write.data;

import de.hdm_stuttgart.mi.read.data.TableData;
import de.hdm_stuttgart.mi.read.schema.model.Column;
import de.hdm_stuttgart.mi.read.schema.model.Table;

import java.sql.SQLException;
import java.util.ArrayList;

public class DataStatementBuilder   {

    /**
     * Get all insert statements from table
     * e. g. {@code INSERT INTO table_name (column1, column2,...) VALUES (value1, value2,...), ...;}
     *
     * @return a list of SQL statement strings which can be used to insert all data
     */

    public static ArrayList<String> dataAsStatement(final TableData tableData) {
        if(tableData == null || tableData.data().size() <= 0) {
            return null;
        }

        ArrayList<String> statements = new ArrayList<>();
        StringBuilder insertQuery = new StringBuilder("INSERT INTO ")
                .append(tableData.schemaName())
                .append(".")
                .append(tableData.tableName())
                .append(" (")
                .append(String.join(", ", tableData.columns()))
                .append(") VALUES ");
        String base = insertQuery.toString();
        //Get values of all rows in Form (value1, value2, ...)
        try {
            int rowCount = 0;
            while (tableData.data().next()) {
                if(rowCount % 100 == 0 && rowCount > 0) {
                    insertQuery.setLength(insertQuery.length() - 2);
                    insertQuery.append(";");
                    statements.add(insertQuery.toString());
                    insertQuery.setLength(0);
                    insertQuery = new StringBuilder(base);
                }
                StringBuilder values = new StringBuilder("(");
                for (int i = 1; i <= tableData.columns().size(); i++) {
                    Object value = tableData.data().getObject(i);
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
            tableData.data().close();
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
