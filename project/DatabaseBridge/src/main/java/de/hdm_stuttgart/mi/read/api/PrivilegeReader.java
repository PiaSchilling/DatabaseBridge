package de.hdm_stuttgart.mi.read.api;

import de.hdm_stuttgart.mi.read.model.ColumnPrivilege;
import de.hdm_stuttgart.mi.read.model.Privilege;

import java.util.ArrayList;

public interface PrivilegeReader {
    ArrayList<Privilege> readTablePrivileges();

    ArrayList<ColumnPrivilege> readColumnPrivileges();
}
