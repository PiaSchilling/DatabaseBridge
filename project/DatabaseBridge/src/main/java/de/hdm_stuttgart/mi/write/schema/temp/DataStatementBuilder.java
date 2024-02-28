package de.hdm_stuttgart.mi.write.schema.temp;

import de.hdm_stuttgart.mi.read.model.Column;
import de.hdm_stuttgart.mi.read.model.Table;

import java.sql.SQLException;
import java.util.ArrayList;

public class DataStatementBuilder   {

    /**
     * Get all insert statements from table
     * e. g. {@code INSERT INTO table_name (column1, column2,...) VALUES (value1, value2,...), ...;}
     *
     * @param table the table for which the sql statement should be built
     * @return a list of SQL statement strings which can be used to insert all data
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
