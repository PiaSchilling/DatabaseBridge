package de.hdm_stuttgart.mi.util.consts;

public class DestinationDbSysConstsLoader extends DbSysConstsLoader{

    private static DbSysConstsLoader INSTANCE;

    public static DbSysConstsLoader INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DestinationDbSysConstsLoader();
        }
        return INSTANCE;
    }
}
