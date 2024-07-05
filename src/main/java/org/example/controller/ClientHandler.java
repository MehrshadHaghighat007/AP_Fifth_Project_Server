package org.example.controller;

import org.example.model.Storage;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class ClientHandler implements Runnable {
    private DatagramSocket serverSocket;
    private DatagramPacket initialPacket;
    private String clientDirectory;

    public ClientHandler(DatagramSocket socket, DatagramPacket packet) {
        this.serverSocket = socket;
        this.initialPacket = packet;
        this.clientDirectory = "clients/" + packet.getAddress().toString().replace("/", "_") + "_" + packet.getPort();
    }

    @Override
    public void run() {
        FileOutputStream fileOutputStream = null;
        try {
            InetAddress clientAddress = initialPacket.getAddress();
            int clientPort = initialPacket.getPort();
            String fileName = new String(initialPacket.getData(), 0, initialPacket.getLength());
//            System.out.println("Received request for file: " + fileName);

            Files.createDirectories(Paths.get(clientDirectory));

            File file = new File(clientDirectory, fileName);
            fileOutputStream = new FileOutputStream(file);

            while (true) {
                byte[] receiveData = new byte[Storage.getPacketSize()];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                int len = receivePacket.getLength();

                if (len == 0) {
                    break;
                }

                new MainThread(new PacketReceiver(fileOutputStream, receivePacket)).start();
            }

//            System.out.println("File received successfully : " + fileName);

            String ackMessage = "File " + fileName + " received successfully";
            byte[] ackData = ackMessage.getBytes();
            DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, clientAddress, clientPort);
            serverSocket.send(ackPacket);

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

