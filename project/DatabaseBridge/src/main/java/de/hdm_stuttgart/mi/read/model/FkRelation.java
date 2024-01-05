package de.hdm_stuttgart.mi.read.model;

public class FkRelation {

    private final String tableName;
    private final String columnName;

    private final FkType fkType;

    /**
     * Models a foreign key relationship to a column of a table
     *
     * @param tableName  the name of the table this FkRelation references
     * @param columnName the name of the column this FkRelation references
     * @param fkType     the type of the key (imported or exported key)
     */
    private FkRelation(String tableName, String columnName, FkType fkType) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.fkType = fkType;
    }

    // TODO comment
    public static FkRelation importedFkRelation(String tableName, String columnName){
        return new FkRelation(tableName,columnName, FkType.IMPORTED);
    }

    public static FkRelation exportedFkRelation(String tableName, String columnName){
        return new FkRelation(tableName,columnName, FkType.EXPORTED);
    }
}
