package org.example.controller;

import org.example.model.Storage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DownloadHandler implements Runnable {
    private DatagramSocket udpServerSocketIII;
    private DatagramPacket receivePacketIII;
    private boolean check;

    public DownloadHandler(DatagramSocket udpServerSocketIII, DatagramPacket receivePacketIII, boolean check) {
        this.udpServerSocketIII = udpServerSocketIII;
        this.receivePacketIII = receivePacketIII;
        this.check = check;
    }

    @Override
    public void run() {
        String username = new String(receivePacketIII.getData(), 0, receivePacketIII.getLength());
        byte[] fileNameByteArray = new byte[Storage.getPacketSize()];
        DatagramPacket fileNamePacket = new DatagramPacket(fileNameByteArray, fileNameByteArray.length);
        try {
            udpServerSocketIII.receive(fileNamePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fileName = new String(fileNamePacket.getData(), 0, fileNamePacket.getLength());
        if (Manager.checkingExistence(username, fileName)) {
            new MainThread(new UDPSender(udpServerSocketIII, fileNamePacket.getAddress(), fileNamePacket.getPort(), "1".getBytes())).start();
            new MainThread(new FileSender(udpServerSocketIII, fileNamePacket.getAddress(), fileNamePacket.getPort(), fileName, username)).start();
        } else {
            new MainThread(new UDPSender(udpServerSocketIII, fileNamePacket.getAddress(), fileNamePacket.getPort(), "0".getBytes())).start();
            run();
        }
        check = false;
    }

    public boolean isCheck() {
        return check;
    }
}
