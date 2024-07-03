package org.example.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSender implements Runnable {
    private InetAddress serverAddress;
    private int serverPort;
    private byte[] data;

    public UDPSender(InetAddress serverAddress, int serverPort, byte[] data) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.data = data;
    }
    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
