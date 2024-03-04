package de.hdm_stuttgart.mi.write.data.api;

import de.hdm_stuttgart.mi.read.data.model.TableData;

import java.util.ArrayList;

public interface DataWriter {
    void writeData(ArrayList<TableData> data);
    String getDDLScript(ArrayList<TableData> data);
}
