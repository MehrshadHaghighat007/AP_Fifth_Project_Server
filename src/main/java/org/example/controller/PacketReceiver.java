package org.example.controller;

import java.io.*;
import java.net.*;

public class PacketReceiver implements Runnable {
    private FileOutputStream fileOutputStream;
    private DatagramPacket receivePacket;

    public PacketReceiver(FileOutputStream fileOutputStream, DatagramPacket packet) {
        this.fileOutputStream = fileOutputStream;
        this.receivePacket = packet;
    }

    @Override
    public void run() {
        try {
            byte[] dataReceived = receivePacket.getData();
            int len = receivePacket.getLength();
            fileOutputStream.write(dataReceived, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

