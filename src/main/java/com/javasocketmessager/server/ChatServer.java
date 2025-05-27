package com.javasocketmessager.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private int port;
    private ServerSocket serverSocket;

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        System.out.println("ChatServer started!");

        /**
         * Creates cached and reusable threads as needed
         */
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                pool.execute(new ChatClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }

        System.out.println("ChatServer stopped!");
    }
}
