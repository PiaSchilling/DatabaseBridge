package de.hdm_stuttgart.mi.read.model;

public class ColumnPrivilege extends Privilege {
    private final String columnName;

    /**
     * Model class for a columnPrivilege
     * @param tableName   name of table this privilege applies to
     * @param grantor     grantor of access
     * @param grantee     grantee of access
     * @param accessType  type of access (DELETE,UPDATE,...)
     * @param columnName  name of column this privilege applies to
     */
    public ColumnPrivilege(String tableName, String grantor, String grantee, AccessType accessType, String columnName) {
        super(tableName, grantor, grantee, accessType);
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return "ColumnPrivilege{" +
                "columnName='" + columnName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", grantor='" + grantor + '\'' +
                ", grantee='" + grantee + '\'' +
                ", accessType=" + accessType +
                '}';
    }
}
