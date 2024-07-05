package org.example;

import org.example.controller.MainThread;
import org.example.controller.TCPHandler;
import org.example.controller.ListHandler;
import org.example.controller.UploadHandler;
import org.example.model.Storage;
import org.example.view.CLI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket tcpServerSocket = new ServerSocket(Storage.getPORT());
            DatagramSocket udpServerSocket = new DatagramSocket(Storage.getPORT());
            System.out.println(CLI.listening(Storage.getPORT()));
            new MainThread(() -> {
                while (true) {
                    try {
                        Socket clientSocket = tcpServerSocket.accept();
                        new MainThread(new TCPHandler(clientSocket)).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new MainThread(() -> {
                while (true) {
                    try {
                        byte[] receiveData = new byte[Storage.getPacketSize()];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        udpServerSocket.receive(receivePacket);
                        new MainThread(new ListHandler(udpServerSocket, receivePacket)).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new MainThread(() -> {
                while (true) {
                    try {
                        byte[] situation = new byte[Storage.getPacketSize()];
                        DatagramPacket receivePacket = new DatagramPacket(situation, situation.length);
                        udpServerSocket.receive(receivePacket);
                        new MainThread(new UploadHandler(udpServerSocket, receivePacket)).start();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
