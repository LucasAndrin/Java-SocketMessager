package com.javasocketmessager.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.javasocketmessager.common.Payload;
import com.javasocketmessager.common.PayloadType;
import com.javasocketmessager.common.User;

public class ChatClientHandler implements Runnable {
    private static final CopyOnWriteArrayList<ChatClientHandler> clients = new CopyOnWriteArrayList<>();
    private static final Gson gson = new Gson();

    private User user;
    private Socket clientSocket;
    private BufferedReader clientSocketIn;
    private PrintWriter clientSocketOut;

    public ChatClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public User user() {
        return user;
    }

    @Override
    public void run() {
        Payload payload;
        try {
            clientSocketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientSocketOut = new PrintWriter(clientSocket.getOutputStream(), true);

            payload = gson.fromJson(clientSocketIn.readLine(), Payload.class);
            user = payload.from();

            clients.add(this);
            broadcast(payload.content("🟢 " + user.name() + " joined the chat!").toJson());

            String message;
            while ((message = clientSocketIn.readLine()) != null) {
                System.out.println("Message received: " + message);
                broadcast(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Client " + user.name() + " desconnected!");
            try {
                clients.remove(this);
                clientSocket.close();

                payload = new Payload();
                payload.type(PayloadType.Left).from(user)
                    .content("🔴 " + user.name() + " left the chat!");

                broadcast(payload.toJson());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void broadcast(String message) {
        Payload payload = gson.fromJson(message, Payload.class);
        for (ChatClientHandler client : clients) {
            if (!payload.from().name().equals(client.user().name()))
                client.clientSocketOut.println(message);
        }
    }
}
