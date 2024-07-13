package org.example.controller;


import org.example.model.Storage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ListHandler implements Runnable {
    private final DatagramSocket udpServerSocketI;
    private final DatagramPacket receivePacketI;
    private boolean check;


    public ListHandler(DatagramSocket udpServerSocketI, DatagramPacket receivePacketI, boolean check) {
        this.udpServerSocketI = udpServerSocketI;
        this.receivePacketI = receivePacketI;
        this.check = check;
    }

    @Override
    public void run() {
        String username = new String(receivePacketI.getData(), 0, receivePacketI.getLength());
        for (String key : Storage.getUsers().keySet()) {
            if (username.equals(key)) {
                for (int i = 0; i < Storage.getUsers().get(username).getFiles().size(); i++) {
                    String fileName = Storage.getUsers().get(username).getFiles().get(i);
                    byte[] fileNameByteArray = fileName.getBytes();
                    new MainThread(new UDPSender(udpServerSocketI, receivePacketI.getAddress(), receivePacketI.getPort(), fileNameByteArray)).start();
                }
                new MainThread(new UDPSender(udpServerSocketI, receivePacketI.getAddress(), receivePacketI.getPort(), new byte[0])).start();
                this.check = false;
            }
        }
    }

    public boolean isCheck() {
        return check;
    }
}
