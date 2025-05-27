package com.javasocketmessager.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.javasocketmessager.common.Payload;
import com.javasocketmessager.common.PayloadType;
import com.javasocketmessager.common.User;

public class MainChatClient {
    private static final String HOST = "172.16.1.96";
    private static final int PORT = 8888;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT)) {
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            Payload payload = new Payload();

            User user = new User();
            System.out.print("Enter your name: ");
            user.name(userInput.readLine());

            payload.type(PayloadType.Enter).from(user);
            socketOut.println(payload.toJson());

            Thread receiveMessages = new Thread(() -> {
                String serverMessage;
                Gson gson = new Gson();
                try {
                    while ((serverMessage = socketIn.readLine()) != null) {
                        Payload payloadMsg = gson.fromJson(serverMessage, Payload.class);
                        System.out.printf("[%s]: %s\n", payloadMsg.from().name(), payloadMsg.content());
                    }
                } catch (IOException e) {
                    System.out.println("Connection finished!");
                }
            });
            receiveMessages.start();

            String content;
            while ((content = userInput.readLine()) != null && !content.equals("#exit")) {
                /**
                 * Output a message into the socket
                 */
                socketOut.println(payload.type(PayloadType.NewMessage).content(content).toJson());
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
