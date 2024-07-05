package org.example.controller;

import org.example.model.Storage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UploadHandler implements Runnable {

    private final DatagramSocket udpServerSocket;
    private final DatagramPacket receivePacket;

    public UploadHandler(DatagramSocket udpServerSocket, DatagramPacket receivePacket) {
        this.udpServerSocket = udpServerSocket;
        this.receivePacket = receivePacket;
    }

    @Override
    public void run() {
        String input = new String(receivePacket.getData(), 0, receivePacket.getLength());
        if (input.equals("1")) {
            try {
                String output = "0";
                byte[] feedback = output.getBytes();
                new MainThread(new UDPSender(udpServerSocket, receivePacket.getAddress(), receivePacket.getPort(), feedback)).start();
                upload();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {

        }
    }

    private void upload() throws IOException {
        while(true) {
            byte[] receiveData = new byte[Storage.getPacketSize()];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            udpServerSocket.receive(receivePacket);
            new MainThread(new ClientHandler(udpServerSocket, receivePacket)).start();
        }
    }
}
