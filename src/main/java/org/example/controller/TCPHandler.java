package org.example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;

    public TCPHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String action = reader.readLine();
            usernameHandler(action);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void usernameHandler(String action) throws IOException {
        String username = reader.readLine();
        if (action.equals("1")) {
            if (SignInAndSignUpManager.signInCheckUsername(username)) {
                writer.println(0);
                passwordSignInHandler(username);
            } else {
                writer.println(1);
                usernameHandler(action);
            }
        } else {
            if (SignInAndSignUpManager.signUpCheck(username)) {
                writer.println(2);
                passwordSignUpHandler(username);
            } else {
                writer.println(3);
                usernameHandler(action);
            }
        }
    }

    private void passwordSignInHandler(String username) throws IOException {
        String password = reader.readLine();
        if (SignInAndSignUpManager.signInCheckPassword(username, password)) {
            writer.println(0);
        } else {
            writer.println(1);
            passwordSignInHandler(username);
        }
    }

    private void passwordSignUpHandler(String username) throws IOException {
        String password = reader.readLine();
        SignInAndSignUpManager.signUp(username, password);
        writer.println(2);
    }
}
