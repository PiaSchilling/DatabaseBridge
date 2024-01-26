package de.hdm_stuttgart.mi.read.api;

import de.hdm_stuttgart.mi.read.model.View;

import java.util.ArrayList;

public interface ViewReader {
    /**
     * Read all views of one schema
     *
     * @param schemaName the name of the schema the views should be read of
     * @return a list of views
     */
    ArrayList<View> readViews(String schemaName);
}
