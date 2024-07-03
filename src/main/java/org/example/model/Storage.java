package org.example.model;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {
    private static final int PORT = 8080;
    private static final int PACKET_SIZE = 1024;
    private static final Map<InetAddress, User> users = new HashMap<>();

    public static int getPORT() {
        return PORT;
    }

    public static int getPacketSize() {
        return PACKET_SIZE;
    }

    public static Map<InetAddress, User> getUsers() {
        return users;
    }
}
