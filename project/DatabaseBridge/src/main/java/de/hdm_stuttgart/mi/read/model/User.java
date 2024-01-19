package de.hdm_stuttgart.mi.read.model;

/**
 * Model class for a single database user
 * TODO maybe add more attributes like password hash
 *
 * @param username the name of the user
 */
public record User(String username) {
}
