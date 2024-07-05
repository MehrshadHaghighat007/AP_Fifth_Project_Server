package org.example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPHandler implements Runnable {
    private Socket clientSocket;
    private InetAddress inetAddress;
    private BufferedReader reader;
    private PrintWriter writer;

    public TCPHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        inetAddress = clientSocket.getInetAddress();
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
            if (Manager.signInCheckUsername(username)) {
                writer.println(0);
                passwordSignInHandler();
            } else {
                writer.println(1);
                usernameHandler(action);
            }
        } else {
            if (Manager.signUpCheck(username)) {
                writer.println(2);
                passwordSignUpHandler(username);
            } else {
                writer.println(3);
                usernameHandler(action);
            }
        }
    }

    private void passwordSignInHandler() throws IOException {
        String password = reader.readLine();
        if (Manager.signInCheckPassword(inetAddress, password)) {
            writer.println(0);
        } else {
            writer.println(1);
            passwordSignInHandler();
        }
    }

    private void passwordSignUpHandler(String username) throws IOException {
        String password = reader.readLine();
        Manager.signUp(inetAddress, username, password);
        writer.println(2);
        clientSocket.close();
    }
}
