package de.hdm_stuttgart.mi.util;

public class SourceDbSysConstsLoader extends DbSysConstsLoader{

    private static DbSysConstsLoader INSTANCE;

    public static DbSysConstsLoader INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new SourceDbSysConstsLoader();
        }
        return INSTANCE;
    }
}
