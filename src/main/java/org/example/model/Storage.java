package org.example.model;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    private static final int PORT_I = 9876;
    private static final int PORT_II = 9877;
    private static final int PORT_III = 9878;
    private static final int PORT_IV = 9879;
    private static final int PORT_V = 9880;
    private static final int PACKET_SIZE = 1024 * 1024;
    private static final Map<String, User> users = new HashMap<>();


    public static int getPortI() {
        return PORT_I;
    }

    public static int getPortIi() {
        return PORT_II;
    }

    public static int getPortIii() {
        return PORT_III;
    }

    public static int getPortIv() {
        return PORT_IV;
    }

    public static int getPortV() {
        return PORT_V;
    }

    public static int getPacketSize() {
        return PACKET_SIZE;
    }

    public static Map<String, User> getUsers() {
        return users;
    }

}
