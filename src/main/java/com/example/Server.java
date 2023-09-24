package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Server extends Thread {
    private int concurrent;
    private int port = 1234; // default port.

    public Server(final int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Start bingo server");

            while (!Thread.currentThread().isInterrupted()) {
                
                Socket socket = serverSocket.accept();
                if (concurrent >= 2) { // 동시 접속 가능한 인원은 2명으로 제한.
                    socket.getOutputStream().write("full".getBytes());
                    socket.close();

                    continue;
                }

                Client client = new Client(socket);
                client.start();

                concurrent++;
                String s = String.format("connected %s:%d", socket.getInetAddress(), socket.getPort());
                System.out.println(s);

                if (concurrent == 2) Client.sendAll("game start!");
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
