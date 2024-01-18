package de.hdm_stuttgart.mi.read.model;

public class Privilege {

    final String tableName;
    final String grantor;
    final String grantee;
    final AccessType accessType;


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
}
