package com.javasocketmessager.server;

public class MainChatServer {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(8888);
        chatServer.start();
    }
}
