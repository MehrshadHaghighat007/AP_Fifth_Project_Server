package org.example.controller;

import org.example.model.Storage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPHandler implements Runnable {
    private DatagramSocket udpServerSocket;
    private DatagramPacket receivePacket;

    public UDPHandler(DatagramSocket udpServerSocket, DatagramPacket receivePacket) {
        this.udpServerSocket = udpServerSocket;
        this.receivePacket = receivePacket;
    }

    @Override
    public void run() {
        String listRequest = new String(receivePacket.getData(), 0, receivePacket.getLength());
        if (listRequest.equals("0")) {
            for (InetAddress inetAddress : Storage.getUsers().keySet()) {
                if (udpServerSocket.getInetAddress().equals(inetAddress)) {
                    for (int i = 0; i < Storage.getUsers().get(inetAddress).getFiles().size(); i++) {
                        new MainThread(new UDPSender(udpServerSocket.getInetAddress(), udpServerSocket.getPort(), Storage.getUsers().get(inetAddress).getFiles().get(i).getBytes())).start();
                    }
                }
            }
        }
    }
}
