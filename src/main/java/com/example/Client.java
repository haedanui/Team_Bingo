package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Client extends Thread {
    public static final List<Client> clients = new ArrayList<>(2);
    public static BingoBoard board = new BingoBoard(5);

    private final int n; // TODO 순번을 뜻하는 변수명으로 rename.
    private final Socket socket;
    private final String mark;

    public Client(final Socket socket, final String mark) {
        this.socket = socket;
        this.mark = mark;

        this.n = clients.size();
        clients.add(this);

        this.setName("user" + clients.size()); // 쓰레드 확인하기 쉽게 이름 변경.
    }

    public void send(String msg) {
        msg += System.lineSeparator();

        try {
            socket.getOutputStream().write(msg.getBytes());
            socket.getOutputStream().flush();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void sendAllWithOutMe(String msg) {
        for (var client : clients)
            if (client != this)
                client.send(msg);
    }

    public static void sendAll(String msg) {
        for (var client : clients)
            client.send(msg);
    }

    public void disconnect() throws IOException {
        for (var client : clients)
            client.getSocket().close();
    }

    @Override
    public void run() {
        try (
                var br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream();) {
            String s; // 유저가 원하는 mark를 사용하게 하고 싶으면 여기에서 br.readLine()로 입력받으면 됩니다.
            while ((s = br.readLine()) != null) {
                if (clients.size() < 2) {
                    send("매칭 대기 중입니다, 기다려주세요");
                    continue;
                }

                if (board.getTurn() % 2 != n) {
                    send("상대방 차례입니다. 기다려 주세요");
                    continue;
                }

                try {
                    int placeIndex = Integer.parseInt(s); // s가 올바른 숫자인지 확인용

                    if (!board.tryPlace(s, mark)) {
                        send("잘못된 위치이거나 이미 사용중인 곳입니다.");
                        continue;
                    }
                    
                    sendAll(board.toString());

                    if (board.checkBingo()) {
                        send("Bingo!!");
                        sendAllWithOutMe("패배");

                        disconnect();
                    }
                    if (board.isDone()) {
                        sendAll("무승부 입니다.");
                        disconnect();
                    }

                    clients.get(board.getTurn() % 2).send("내 차례입니다.");

                } catch (NumberFormatException e) {
                    send("숫자만 입력해주세요.");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
