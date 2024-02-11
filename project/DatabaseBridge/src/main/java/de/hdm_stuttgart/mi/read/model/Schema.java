package de.hdm_stuttgart.mi.read.model;

import java.util.ArrayList;

/**
 * Model class for a single database schema
 *
 * @param tables           the database tables of this schema
 * @param views            the views of this schema
 * @param users            the users of this schema
 * @param tablePrivileges  the access privileges of users on table level
 * @param columnPrivileges the access privileges of users on table column level
 */
public record Schema(String name, ArrayList<Table> tables, ArrayList<View> views, ArrayList<User> users,
                     ArrayList<Privilege> tablePrivileges, ArrayList<ColumnPrivilege> columnPrivileges) {
}
