package org.example.controller;

import org.example.model.Storage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UploadHandler implements Runnable {
    private DatagramSocket udpServerSocketII;
    private final DatagramPacket receivePacketII;
    private boolean check;

    public UploadHandler(DatagramSocket udpServerSocketII, DatagramPacket receivePacketII, boolean check) {
        this.udpServerSocketII = udpServerSocketII;
        this.receivePacketII = receivePacketII;
        this.check = check;
    }

    @Override
    public void run() {
        String username = new String(receivePacketII.getData(), 0, receivePacketII.getLength());
        byte[] receiveData = new byte[Storage.getPacketSize()];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            udpServerSocketII.receive(receivePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fileName = new String(receivePacket.getData(), 0, receivePacket.getLength());
        if (Manager.checkingDuplication(username, fileName)) {
            new MainThread(new UDPSender(udpServerSocketII, receivePacket.getAddress(), receivePacket.getPort(), "1".getBytes())).start();
            new MainThread(new ClientHandler(udpServerSocketII, receivePacket, username)).start();
            Storage.getUsers().get(username).getFiles().add(fileName);
        } else {
            new MainThread(new UDPSender(udpServerSocketII, receivePacket.getAddress(), receivePacket.getPort(), "0".getBytes())).start();
            run();
        }
        check = false;
    }

    public boolean isCheck() {
        return check;
    }
}
