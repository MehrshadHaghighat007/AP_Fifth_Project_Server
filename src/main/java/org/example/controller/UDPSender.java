package org.example.controller;

import org.example.model.Storage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSender implements Runnable {
    private DatagramSocket udpServerSocket;
    private InetAddress address;
    private int port;
    private byte[] data;

    public UDPSender(DatagramSocket udpServerSocket, InetAddress address, int port, byte[] data) {
        this.udpServerSocket = udpServerSocket;
        this.address = address;
        this.port = port;
        this.data = data;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < data.length; i += Storage.getPacketSize()) {
                int end = Math.min(data.length, i + Storage.getPacketSize());
                byte[] chunk = new byte[end - i];
                for (int j = 0; j < end - i; j++) {
                    chunk[j] = data[i + j];
                }
                DatagramPacket packet = new DatagramPacket(chunk, chunk.length, address, port);
                udpServerSocket.send(packet);
            }
//                byte[] endMessage = new byte[0];
//                DatagramPacket endPacket = new DatagramPacket(endMessage, endMessage.length, address, port);
//                udpServerSocket.send(endPacket);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
