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
                DatagramSocket udpServerSocketV = new DatagramSocket(Storage.getPortV());
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

                new MainThread(() -> {
                    while (true) {
                        try {
                            byte[] receiveDataV = new byte[Storage.getPacketSize()];
                            DatagramPacket receivePacketV = new DatagramPacket(receiveDataV, receiveDataV.length);
                            udpServerSocketV.receive(receivePacketV);
                            ListHandler listHandler = new ListHandler(udpServerSocketV, receivePacketV, check.get());
                            new MainThread(listHandler).start();
//                            Thread.sleep(2000);
//                            check.set(listHandler.isCheck());
//                            System.out.println(check.get());
//                            if (!check.get()) {
//                                System.out.println("oomad");
//                                break;
//                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                while (true) {
                    System.out.println("residam");
                    byte[] uploadOrDownloadByteArray = new byte[Storage.getPacketSize()];
                    DatagramPacket uploadOrDownloadPacket = new DatagramPacket(uploadOrDownloadByteArray, uploadOrDownloadByteArray.length);
                    udpServerSocketIV.receive(uploadOrDownloadPacket);
                    String input = new String(uploadOrDownloadPacket.getData(), 0, uploadOrDownloadPacket.getLength());
                    udpServerSocketIV.close();
                    if (input.equals("1")) {
                        byte[] receiveDataII = new byte[Storage.getPacketSize()];
                        DatagramPacket receivePacketII = new DatagramPacket(receiveDataII, receiveDataII.length);
                        udpServerSocketII.receive(receivePacketII);
                        UploadHandler uploadHandler = new UploadHandler(udpServerSocketII, receivePacketII);
                        new MainThread(uploadHandler).start();
                        break;
                    } else {
                        byte[] receiveDataIII = new byte[Storage.getPacketSize()];
                        DatagramPacket receivePacketIII = new DatagramPacket(receiveDataIII, receiveDataIII.length);
                        udpServerSocketIII.receive(receivePacketIII);
                        DownloadHandler downloadHandler = new DownloadHandler(udpServerSocketIII, receivePacketIII);
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
