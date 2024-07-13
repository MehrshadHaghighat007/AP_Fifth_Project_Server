package org.example.controller;

import org.example.model.Storage;
import org.example.view.CLI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FileSender implements Runnable {
    private DatagramSocket udpServerSocketII;
    private InetAddress address;
    private int port;
    private String fileName;
    private String path;

    public FileSender(DatagramSocket udpServerSocketII, InetAddress address, int port, String fileName, String username) {
        this.udpServerSocketII = udpServerSocketII;
        this.address = address;
        this.port = port;
        this.fileName = fileName;
        path = "/home/mehrshad/Downloads/Location/" + username + "/" + fileName;
    }

    @Override
    public void run() {
        FileInputStream fileInputStream = null;
        try {
            new MainThread(new UDPSender(udpServerSocketII, address, port, fileName.getBytes())).start();
            File file = new File(path);

            fileInputStream = new FileInputStream(file);
            byte[] sendData = new byte[Storage.getPacketSize()];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(sendData)) != -1) {
                byte[] packetData = new byte[bytesRead];
                System.arraycopy(sendData, 0, packetData, 0, bytesRead);
                new MainThread(new UDPSender(udpServerSocketII, address, port, packetData)).start();
                sendData = new byte[Storage.getPacketSize()];
            }

            new MainThread(new UDPSender(udpServerSocketII, address, port, new byte[0])).start();

            System.out.println(CLI.fileSent(fileName));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
