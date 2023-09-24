package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {
    private static final List<Client> clients = new ArrayList<>(2);
    private static int turn; // TODO BingoBoard board로 변경.

    private final int n; // TODO 순번을 뜻하는 변수명으로 rename.
    private final Socket socket;

    public Client(final Socket socket) {
        this.socket = socket;

        this.n = clients.size();
        clients.add(this);

        this.setName("user" + clients.size()); // 쓰레드 확인하기 쉽게 이름 변경.
    }

    public synchronized void inc() { // 임시 함수.
        turn++;
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

    public static void sendAll(String msg) {
        // msg += System.lineSeparator();

        for (var client : clients)
            client.send(msg);
    }

    @Override
    public void run() {
        try (
                var br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream();
        ) {
            String s; // 유저가 원하는 mark를 사용하게 하고 싶으면 여기에서 br.readLine()로 입력받으면 됩니다.
            while ((s = br.readLine()) != null) {
                if (turn % 2 != n) { // TODO bingo.getTurn() % 2 != n.
                    send("상대방 차례입니다. 기다려 주세요");
                    continue;
                }

                try {
                    // TODO add bingo logic.
                    int placeIndex = Integer.parseInt(s);
                    System.out.println("put : " + placeIndex); inc(); // 임시.

                } catch (NumberFormatException e) {
                    send("숫자만 입력해주세요.");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
