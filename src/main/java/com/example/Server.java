package com.example;

import java.net.ServerSocket;
import java.net.Socket;

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

        try (ServerSocket serverSocket = new ServerSocket(port);) {
            System.out.println("Start bingo server");

            while (!Thread.currentThread().isInterrupted()) {

                Socket socket = serverSocket.accept();
                if (concurrent >= 2) { // 동시 접속 가능한 인원은 2명으로 제한.
                    socket.getOutputStream().write(("정원이 가득 찼습니다" + System.lineSeparator()).getBytes());
                    socket.getOutputStream().flush();

                    socket.close();

                    continue;
                }

                Client client = new Client(socket, ((concurrent % 2 == 0) ? "O" : "X"));
                client.start();

                concurrent++;
                String s = String.format("connected %s:%d", socket.getInetAddress(), socket.getPort());
                System.out.println(s);

                if (concurrent == 2) {
                    Client.sendAll("게임 시작!");
                    Client.sendAll(Client.board.toString());

                    Client.clients.get(0).send("내 차례입니다.");
                }
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
