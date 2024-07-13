package org.example;

import org.example.controller.*;
import org.example.model.Storage;
import org.example.view.CLI;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    public static void main(String[] args) {
        while (true) {
            AtomicBoolean check = new AtomicBoolean(true);
            try {
                ServerSocket tcpServerSocket = new ServerSocket(Storage.getPortI());
                DatagramSocket udpServerSocketI = new DatagramSocket(Storage.getPortI());
                DatagramSocket udpServerSocketII = new DatagramSocket(Storage.getPortIi());
                DatagramSocket udpServerSocketIII = new DatagramSocket(Storage.getPortIii());
                DatagramSocket udpServerSocketIV= new DatagramSocket(Storage.getPortIv());

                System.out.println(CLI.listening(Storage.getPortI()));

                new MainThread(() -> {
                    while (true) {
                        try {
                            Socket clientSocket = tcpServerSocket.accept();
                            new MainThread(new TCPHandler(clientSocket)).start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                while (true) {
                    try {
                        byte[] receiveDataI = new byte[Storage.getPacketSize()];
                        DatagramPacket receivePacketI = new DatagramPacket(receiveDataI, receiveDataI.length);
                        udpServerSocketI.receive(receivePacketI);
                        ListHandler listHandler = new ListHandler(udpServerSocketI, receivePacketI, check.get());
                        new MainThread(listHandler).start();
                        Thread.sleep(10000);
                        check.set(listHandler.isCheck());
                        System.out.println(check.get());
                        if (!check.get()) {
                            System.out.println("oomad");
                            break;
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                while (true) {
                    System.out.println("residam");
                    byte[] uploadOrDownloadByteArray = new byte[Storage.getPacketSize()];
                    DatagramPacket uploadOrDownloadPacket = new DatagramPacket(uploadOrDownloadByteArray, uploadOrDownloadByteArray.length);
                    udpServerSocketIV.receive(uploadOrDownloadPacket);
                    String input = new String(uploadOrDownloadPacket.getData(), 0, uploadOrDownloadPacket.getLength());
                    if (input.equals("1")) {
                        byte[] receiveDataII = new byte[Storage.getPacketSize()];
                        DatagramPacket receivePacketII = new DatagramPacket(receiveDataII, receiveDataII.length);
                        udpServerSocketII.receive(receivePacketII);
                        UploadHandler uploadHandler = new UploadHandler(udpServerSocketII, receivePacketII, check.get());
                        new MainThread(uploadHandler).start();
                        break;
                    } else {
                        byte[] receiveDataIII = new byte[Storage.getPacketSize()];
                        DatagramPacket receivePacketIII = new DatagramPacket(receiveDataIII, receiveDataIII.length);
                        udpServerSocketIII.receive(receivePacketIII);
                        DownloadHandler downloadHandler = new DownloadHandler(udpServerSocketIII, receivePacketIII, check.get());
                        new MainThread(downloadHandler).start();
                        break;
                    }
                }

                check.set(true);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
