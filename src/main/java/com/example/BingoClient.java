package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class BingoClient extends Thread{
    BufferedReader reader;
    BufferedWriter writer;

    public BingoClient(BufferedReader reader, BufferedWriter writer){
        this.reader =reader;
        this.writer = writer;
    }
    @Override
    public void run(){
        String line;
        try {
            while((line = reader.readLine()) != null){
                writer.write(line + "\n");
                writer.flush();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 1232;
        try (
            Socket socket= new Socket(host, port);

            BufferedReader usreIn = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter userOut = new BufferedWriter(new OutputStreamWriter(System.out));
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {
            BingoClient terInsockOut = new BingoClient(usreIn, socketOut);
            BingoClient sockInterOut = new BingoClient(socketIn, userOut);

            terInsockOut.start();
            sockInterOut.start();

            terInsockOut.join();
            sockInterOut.join();
            
            
        } catch (Exception e) {
            System.out.println("서버 연결 실패");
            System.out.println(e);
        }
    }
}
