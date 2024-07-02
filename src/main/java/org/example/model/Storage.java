package org.example.model;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private static final int PORT = 8080;
    private static final int PACKET_SIZE = 1024;
    private static final Map<String, User> users = new HashMap<>();

    public static int getPORT() {
        return PORT;
    }

    public static int getPacketSize() {
        return PACKET_SIZE;
    }

    public static Map<String, User> getUsers() {
        return users;
    }
}
