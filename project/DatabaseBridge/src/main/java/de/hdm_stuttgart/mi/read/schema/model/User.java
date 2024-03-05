package de.hdm_stuttgart.mi.read.schema.model;

/**
 * Model class for a single database user
 *
 * @param username the name of the user
 * @param password the password of the user
 * @param isCurrentUser true if this user represents the current user (the user, whose login credentials are used to log
 *                     in to the sourceDB) false otherwise
 *
 */
public record User(String username,String password,boolean isCurrentUser) {
}
