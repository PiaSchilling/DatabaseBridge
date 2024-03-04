package de.hdm_stuttgart.mi.write.schema.implementation;

import de.hdm_stuttgart.mi.read.schema.model.*;
import de.hdm_stuttgart.mi.util.consts.DestinationConsts;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SchemaStatementBuilder {

    public static String dropSchemaStatement(final String schemaName) {
        return DestinationConsts.dropSchemaStmt(schemaName);
    }

    public static String createSchemaStatement(final String schemaName) {
        return "CREATE SCHEMA " + schemaName + ";";
    }

    /**
     * Get the CREATE TABLE statement for the provided {@code table}
     *
     * @param table      the table for which the sql statement should be built
     * @param schemaName the name of the schema the table belongs to
     * @return a SQL statement string which contains all attributes of this table
     * @example {@code CREATE TABLE departments(dept_no CHAR(4) NOT NULL UNIQUE,dept_name VARCHAR(40) NOT NULL UNIQUE,PRIMARY KEY (dept_no))}
     */
    public static String createTableStatement(final Table table, final String schemaName) {
        final String columnString = table.columns().stream().map(SchemaStatementBuilder::columnsAsStatement).collect(Collectors.joining());

        final ArrayList<Column> primaryKeys = table.primaryKeys();
        final String pkString = primaryKeys.isEmpty()
                ? ""
                : "PRIMARY KEY (" + String.join(",", primaryKeys.stream().map(Column::name).toList()) + ")";

        String createTableString = "CREATE TABLE " + schemaName + "." + table.name() + "(" + columnString + pkString;

        if (createTableString.endsWith(",")) {
            createTableString = createTableString.substring(0, createTableString.lastIndexOf(","));
        }

        return createTableString + ");";
    }

    /**
     * Get the DROP TABLE statement for the provided {@code table}
     *
     * @param table      the table for which the sql statement should be built
     * @param schemaName the name of the schema the table belongs to
     * @return a SQL statement string which can be used to delete the provided {@code table}
     */
    public static String dropTableStatement(final Table table, final String schemaName) {
        return "DROP TABLE IF EXISTS " + schemaName + "." + table.name() + " CASCADE;";
    }

    /**
     * Get the CREATE OR REPLACE VIEW statement for the provided {@code view}
     *
     * @param view       the view for which the sql statement should be built
     * @param schemaName the name of the schema the view belongs to
     * @return a SQL statement string which can be used to build the provided {@code view}
     */
    public static String createViewStatement(final View view, final String schemaName) {
        return "CREATE OR REPLACE VIEW " + schemaName + "." + view.name() + " AS" + view.createViewStatement();
    }

    /**
     * Get all ALTER TABLE ADD CONSTRAINT statements for the provided tables fk relations
     *
     * @param table      the table for which the sql statements should be built
     * @param schemaName the name of the schema the provided table belongs to
     * @return a SQL statement string which can be used to add all fk relations via ALTER TABLE
     * @example {@code ALTER TABLE ecommerce.order_items ADD CONSTRAINT order_items_order_id_fkey FOREIGN KEY(order_id) REFERENCES ecommerce.orders(order_id) ON UPDATE NO ACTION ON DELETE NO ACTION;
     * ALTER TABLE ecommerce.order_items ADD CONSTRAINT order_items_product_id_fkey FOREIGN KEY(product_id) REFERENCES ecommerce.products(product_id) ON UPDATE NO ACTION ON DELETE NO ACTION;}
     */
    public static String alterTableAddFkRelationStatement(final Table table, final String schemaName) {
        if (!table.isChildTable()) {
            return "";
        }

        return table.importedFkRelations()
                .stream()
                .map(relation -> fkRelationAsStatement(relation, schemaName))
                .map(e -> "ALTER TABLE " + schemaName + "." + table.name() + " ADD CONSTRAINT " + e + ";")
                .collect(Collectors.joining());
    }

    /**
     * Get single ALTER TABLE ADD CONSTRAINT statements for the provided fk relation
     *
     * @param table      the table for which the sql statement should be built
     * @param relation   the relation for which the sql statement should be built
     * @param schemaName the name of the schema the provided table belongs to
     * @return a SQL statement string which can be used to add all fk relations via ALTER TABLE
     * @example {@code ALTER TABLE titles ADD CONSTRAINT FOREIGN KEY(emp_no) REFERENCES employees(emp_no) ON UPDATE RESTRICT ON DELETE CASCADE;}
     */
    public static String singleAlterTableAddFkRelationStatement(final Table table, final FkRelation relation, final String schemaName) {
        return "ALTER TABLE " + schemaName + "." + table.name() + " ADD CONSTRAINT " + fkRelationAsStatement(relation, schemaName) + ";";
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

        // Remove default constraint, if the column is autoincrement
        column.constraints().removeIf(c ->
                c.getConstraintType() == ConstraintType.DEFAULT &&
                        column.constraints().stream().anyMatch(innerC -> innerC.getConstraintType() == ConstraintType.AUTO_INKREMENT)
        );


        final String constraintString = column.constraints().stream()
                .filter(constraint -> constraint.getConstraintType() != ConstraintType.PRIMARY_KEY)
                .map(SchemaStatementBuilder::constraintAsStatement)
                .collect(Collectors.joining(" "));

        final String dbSpecificType = DestinationConsts.getType(column.dataType());

        // special case: auto increment (resp. serial) columns in postgres may not define the datatype and default constraint
        if (constraintString.contains("SERIAL")) {
            return "\n" + column.name() + " " + constraintString.replaceAll("DEFAULT.*", "") + ",";
        }
        return column.dataType().hasLength ? "\n" + column.name() + " " + dbSpecificType + "(" + column.maxLength() + ")" + " " + constraintString + "," :
                "\n" + column.name() + " " + dbSpecificType + " " + constraintString + ",";
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
            case AUTO_INKREMENT -> DestinationConsts.autoIncrementConstraintName;
        };
    }

    /**
     * Get a fkRelation as statement representation which can be used to build the relations in a CREATE TABLE statement
     *
     * @param fkRelation the fkRelation for which the sql statement should be built
     * @return a SQL statement string containing all attributes of this fk relation
     * @example {@code REFERENCES employees(emp_no) ON UPDATE RESTRICT ON DELETE CASCADE}
     */
    private static String fkRelationAsStatement(final FkRelation fkRelation, final String schemaName) {
        return fkRelation.fkName() + " FOREIGN KEY(" + fkRelation.referencingColumnName() + ")"
                + " REFERENCES " + schemaName + "." + fkRelation.tableName() + "(" + fkRelation.referencedColumnName()
                + ") ON UPDATE " + fkRelation.updateRule().asString
                + " ON DELETE " + fkRelation.deleteRule().asString;
    }
}