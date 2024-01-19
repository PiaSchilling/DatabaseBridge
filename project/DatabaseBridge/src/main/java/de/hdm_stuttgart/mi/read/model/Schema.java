package de.hdm_stuttgart.mi.read.model;

import java.util.ArrayList;

public class Schema {
    private final ArrayList<Table> tables;
    private final ArrayList<View> views;
    private final ArrayList<User> users;

    private final ArrayList<Privilege> tablePrivileges;

    private final ArrayList<ColumnPrivilege> columnPrivileges;

    // TODO comment
    public Schema(ArrayList<Table> tables,
                  ArrayList<View> views,
                  ArrayList<User> users,
                  ArrayList<Privilege> tablePrivileges,
                  ArrayList<ColumnPrivilege> columnPrivileges) {
        this.tables = tables;
        this.views = views;
        this.users = users;
        this.tablePrivileges = tablePrivileges;
        this.columnPrivileges = columnPrivileges;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public ArrayList<View> getViews() {
        return views;
    }
}
