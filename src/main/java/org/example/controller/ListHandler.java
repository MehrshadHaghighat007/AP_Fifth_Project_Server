package org.example.controller;

import org.example.model.Storage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ListHandler implements Runnable {
    private final DatagramSocket udpServerSocket;
    private final DatagramPacket receivePacket;

    public ListHandler(DatagramSocket udpServerSocket, DatagramPacket receivePacket) {
        this.udpServerSocket = udpServerSocket;
        this.receivePacket = receivePacket;
    }

    @Override
    public void run() {
        String listRequest = new String(receivePacket.getData(), 0, receivePacket.getLength());
        if (listRequest.equals("0")) {
            for (InetAddress inetAddress : Storage.getUsers().keySet()) {
                if ((receivePacket.getAddress() == inetAddress)) {
                    for (int i = 0; i < Storage.getUsers().get(inetAddress).getFiles().size(); i++) {
                        String fileName = Storage.getUsers().get(inetAddress).getFiles().get(i);
                        byte[] fileNameByteArray = fileName.getBytes();
                        int offset = 0;
                        while (offset < fileNameByteArray.length) {
                            int length = Math.min(Storage.getPacketSize(), fileNameByteArray.length - offset);
                            byte[] sendData = new byte[length];
                            System.arraycopy(fileNameByteArray, offset, sendData, 0, length);
                            new MainThread(new UDPSender(udpServerSocket, receivePacket.getAddress(), receivePacket.getPort(), sendData)).start();
                            offset += length;
                        }
                    }
                } else {
                    byte[] zero = "0".getBytes();
                    new MainThread(new UDPSender(udpServerSocket, receivePacket.getAddress(), receivePacket.getPort(), zero)).start();
                }
            }
        }
    }
}
