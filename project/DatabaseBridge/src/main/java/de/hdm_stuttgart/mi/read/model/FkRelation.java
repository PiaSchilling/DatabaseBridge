package de.hdm_stuttgart.mi.read.model;

public class FkRelation {

    private final String fkName;
    private final String tableName;
    private final String columnName;


    /**
     * Models a foreign key relationship to a column of a table
     *
     * @param tableName  the name of the table this FkRelation references
     * @param columnName the name of the column this FkRelation references
     * @param fkName     the name of the foreign key
     */
    public FkRelation(String tableName, String columnName, String fkName) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.fkName = fkName;
    }

    @Override
    public String toString() {
        return "FkRelation{" +
                "fkName='" + fkName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                '}';
    }
}
