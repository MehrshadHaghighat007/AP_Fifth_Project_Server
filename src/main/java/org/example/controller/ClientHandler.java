package org.example.controller;

import org.example.model.Storage;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class ClientHandler implements Runnable {
    private DatagramSocket serverSocketII;
    private DatagramPacket initialPacket;
    private String clientDirectory;

    public ClientHandler(DatagramSocket socket, DatagramPacket packet, String username) {
        this.serverSocketII = socket;
        this.initialPacket = packet;
        this.clientDirectory = "/home/mehrshad/Downloads/Location/" + username;
    }

    @Override
    public void run() {
        FileOutputStream fileOutputStream = null;
        try {
            String fileName = new String(initialPacket.getData(), 0, initialPacket.getLength());
            System.out.println("Received request for file: " + fileName);

            Files.createDirectories(Paths.get(clientDirectory));

            File file = new File(clientDirectory, fileName);

            fileOutputStream = new FileOutputStream(file);

            while (true) {
                byte[] receiveData = new byte[Storage.getPacketSize()];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocketII.receive(receivePacket);
                int len = receivePacket.getLength();

                if (len == 0) {
//                    String ackMessage = "File " + fileName + " received successfully";
//                    new MainThread(new UDPSender(serverSocketII, initialPacket.getAddress(), initialPacket.getPort(), ackMessage.getBytes())).start();
                    break;
                }

                new MainThread(new PacketReceiver(fileOutputStream, receivePacket)).start();
            }

            System.out.println("File received successfully : " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

