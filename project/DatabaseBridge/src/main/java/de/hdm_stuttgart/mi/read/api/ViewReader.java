package de.hdm_stuttgart.mi.read.api;

import de.hdm_stuttgart.mi.read.model.View;

import java.util.ArrayList;

public interface ViewReader {
    ArrayList<View> readViews(String schemaName);
}
