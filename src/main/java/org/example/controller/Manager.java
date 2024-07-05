package org.example.controller;

import org.example.model.Storage;
import org.example.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.net.InetAddress;

public class Manager {
    public static boolean signInCheckUsername(String username) {
        for (User user : Storage.getUsers().values()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static boolean signUpCheck(String username) {
        for (User user : Storage.getUsers().values()) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public static boolean signInCheckPassword(InetAddress inetAddress, String password) {
        return BCrypt.checkpw(password, Storage.getUsers().get(inetAddress).getHashedPassword());
    }

    public static void signUp(InetAddress inetAddress, String username, String password) {
        Storage.getUsers().put(inetAddress, new User(username, BCrypt.hashpw(password, BCrypt.gensalt())));
    }
}
