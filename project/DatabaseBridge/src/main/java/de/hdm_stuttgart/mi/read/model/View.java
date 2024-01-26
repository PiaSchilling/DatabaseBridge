package de.hdm_stuttgart.mi.read.model;


public class View {
    private final String name;
    private final String createViewStatement;

    public View(String name, String createViewStatement) {
        this.name = name;
        this.createViewStatement = createViewStatement;
    }

    @Override
    public String toString() {
        return "View{" +
                "name='" + name + '\'' +
                ", createViewStatement='" + createViewStatement + '\'' +
                '}';
    }
}
