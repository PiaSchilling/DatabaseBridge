package de.hdm_stuttgart.mi.read.schema.model;


/**
 * Model class for a database view
 * @param name the name of the view
 * @param createViewStatement the sql statement which was used to initially create the view
 */
public record View (String name, String createViewStatement){
}
