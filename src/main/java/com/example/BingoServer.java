package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class BingoServer extends Thread{
    private static List<BingoServer> serverList = new LinkedList<>();
    private BingoBoard bingo;
    private Socket socket;
    

    public BingoServer(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run(){

    }
    
    public static void main(String[] args) {//서버 자체를 구동하기 위한 main 함수
        int port = 1234; // 포트 입력 함수 추가시 제가 바람
       try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket = serverSocket.accept();
            new BingoServer(socket);
        
       } catch (Exception e) {
        // TODO: handle exception
       }
    }
}
