package org.example.controller;


import org.example.model.Storage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ListHandler implements Runnable {
    private final DatagramSocket udpServerSocketV;
    private final DatagramPacket receivePacketV;
    private boolean check;


    public ListHandler(DatagramSocket udpServerSocketV, DatagramPacket receivePacketV, boolean check) {
        this.udpServerSocketV = udpServerSocketV;
        this.receivePacketV = receivePacketV;
        this.check = check;
    }

    @Override
    public void run() {
        String username = new String(receivePacketV.getData(), 0, receivePacketV.getLength());
        for (String key : Storage.getUsers().keySet()) {
            if (username.equals(key)) {
                for (int i = 0; i < Storage.getUsers().get(username).getFiles().size(); i++) {
                    String fileName = Storage.getUsers().get(username).getFiles().get(i);
                    byte[] fileNameByteArray = fileName.getBytes();
                    new MainThread(new UDPSender(udpServerSocketV, receivePacketV.getAddress(), receivePacketV.getPort(), fileNameByteArray)).start();
                }
                new MainThread(new UDPSender(udpServerSocketV, receivePacketV.getAddress(), receivePacketV.getPort(), new byte[0])).start();
                this.check = false;
            }
        }
    }

    public boolean isCheck() {
        return check;
    }
}
