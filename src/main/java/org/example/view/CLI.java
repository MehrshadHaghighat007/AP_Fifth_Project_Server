package org.example.view;

public class CLI {
    public static String listening(int port) {
        return "Server is listening on the : " + port;
    }

    public static String fileSent(String fileName) {
        return "File sent successfully : " + fileName;
    }
}
