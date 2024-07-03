package org.example.controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
            for (int i = 0; i < )
            udpServerSocket.send();
        }
    }
}
