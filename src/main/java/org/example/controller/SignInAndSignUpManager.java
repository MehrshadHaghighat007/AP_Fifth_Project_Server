package org.example.controller;

import org.example.model.Storage;
import org.example.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class SignInAndSignUpManager {
    public static boolean signInCheckUsername(String username) {
        return Storage.getUsers().containsKey(username);
    }

    public static boolean signUpCheck(String username) {
        return !Storage.getUsers().containsKey(username);
    }

    public static boolean signInCheckPassword(String username, String password) {
        return BCrypt.checkpw(password, Storage.getUsers().get(username).getHashedPassword());
    }

    public static void signUp(String username, String password) {
        Storage.getUsers().put(username, new User(username, BCrypt.hashpw(password, BCrypt.gensalt())));
    }
}
