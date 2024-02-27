package de.hdm_stuttgart.mi.read.schema.model;

public class Privilege {

    private final String tableName;
    private final String grantor;
    private final String grantee;
    private final AccessType accessType;


    /**
     * Model class for a tablePrivilege
     *
     * @param tableName  name of table this privilege applies to
     * @param grantor    grantor of access
     * @param grantee    grantee of access
     * @param accessType type of access (DELETE,UPDATE,...)
     */
    public Privilege(String tableName, String grantor, String grantee, AccessType accessType) {
        this.tableName = tableName;
        this.grantor = grantor;
        this.grantee = grantee;
        this.accessType = accessType;
    }

    @Override
    public String toString() {
        return "Privilege{" +
                "tableName='" + tableName + '\'' +
                ", grantor='" + grantor + '\'' +
                ", grantee='" + grantee + '\'' +
                ", accessType=" + accessType +
                '}';
    }

    public String getTableName() {
        return tableName;
    }

    public String getGrantor() {
        return grantor;
    }

    public String getGrantee() {
        return grantee;
    }

    public AccessType getAccessType() {
        return accessType;
    }
}
