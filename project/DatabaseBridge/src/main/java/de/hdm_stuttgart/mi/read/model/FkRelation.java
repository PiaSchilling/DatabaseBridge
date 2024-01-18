package de.hdm_stuttgart.mi.read.model;

public class FkRelation {

    private final String fkName;
    private final String tableName;
    private final String columnName;

    private final DeleteUpdateRule updateRule;
    private final DeleteUpdateRule deleteRule;


    /**
     * Models a foreign key relationship to a column of a table
     *
     * @param tableName  the name of the table this FkRelation references
     * @param columnName the name of the column this FkRelation references
     * @param fkName     the name of the foreign key
     * @param updateRule what happens to a foreign key when the primary key is updated
     * @param deleteRule What happens to a foreign key when the primary key is deleted
     */
    public FkRelation(String tableName, String columnName, String fkName, DeleteUpdateRule updateRule, DeleteUpdateRule deleteRule) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.fkName = fkName;
        this.updateRule = updateRule;
        this.deleteRule = deleteRule;
    }

    @Override
    public String toString() {
        return "FkRelation{" +
                "fkName='" + fkName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", updateRule=" + updateRule +
                ", deleteRule=" + deleteRule +
                '}';
    }
}
