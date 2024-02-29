package de.hdm_stuttgart.mi.read.schema.api;

import de.hdm_stuttgart.mi.read.schema.model.User;

import java.util.ArrayList;

public interface UsersReader {

    /**
     * Read all users from the database
     *
     * @return a list of all users
     */
    ArrayList<User> readUsers();
}
