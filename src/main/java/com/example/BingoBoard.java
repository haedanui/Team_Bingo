package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
public class BingoBoard {
    private static final StringBuilder SB = new StringBuilder();
    private static int boardsize; //static 을 선언하지 않으면 문제가 생기려나?(생길시 수정 바람)

    private static String[][] board;
    
    
    public BingoBoard(int boardsize) {
        this.boardsize = boardsize;
        board = new String[boardsize][boardsize];
        initBoard();
    }

    //
    private void initBoard() {
        HashSet<Integer> set = new HashSet<>(boardsize * boardsize);
        List<Integer> list = new ArrayList<>();
        //TODO size를 받아서 적당한 범위의 값을 책정해 줄 것!
        int range = boardsize * boardsize;//범위 범위를 size*size를 하면 범위가 정해진다.
        int rand;//랜덤값을 임시로 저장하는 곳.
        for(int i = 0; i < range; i++){
            while (set.contains(rand = (int)((Math.random()*range)+1))) {
                //random에 +1을 하면 0에서 24까지 하는걸 1에서 25 사이로설정 가능
                // 값이 중복이면 아닌 값을 뽑을때 까지 반복
            }
            set.add(rand);
            list.add(rand);
        }

        int index = 0;
        for (int n: list){
            board[index/boardsize][index%boardsize] = String.valueOf(n);
            index++;
        }
        // Iterator iterator = set.iterator();
        // for(int i = 0; i < range; i++){
        //     board[i/boardsize][i%boardsize] = String.valueOf(iterator.next());
        // }
    }

    //TODO 빙고의 여부확인을 체크하는 함수 추가
    
    public static boolean bingoCheck(String figure){
        //우선 가로 빙고 확인
        for(int i = 0 ; i < boardsize; i++){
            int count = 0;
            for(int j = 0; j < boardsize ; j++){
                if(board[i][j].charAt(0)=='['){
                    count++;
                }
            }
            if(count == boardsize){
                return true;
            }
        }

        //다음 세로 빙고 확인
        for(int j = 0; j < boardsize; j++){
            int count = 0;
            for(int i = 0 ; i < boardsize ; i++){
                if(board[i][j].charAt(0)=='['){
                    count++;
                }
            }
            if(count == boardsize){
                return true;
            }
        }
        int count = 0;
        //11시에서 5시로 가는 대각선
        for(int i = 0,j = 0; i<boardsize; i++, j++){
            if(board[i][j].charAt(0)=='['){
                count++;
            }
            if(count == boardsize){
                return true;
            }
        }
        count = 0;
        //1시에서 7시로 가는 대각선
        for(int i = 0, j = 4; i <boardsize; i++, j--){
            if(board[i][j].charAt(0)=='['){
                count++;
            }
            if(count  == boardsize){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        SB.setLength(0); // init.
        int range = boardsize * boardsize;

        for (int i = 0; i < range ; ++i) {
            SB.append(String.format("%5s", board[i / boardsize][i % boardsize]));
            if ((i + 1) % boardsize == 0)
                SB.append('\n');
        }

        return SB.toString();
    }

    public static void main(String[] args) {
        //System.out.println(new BingoBoard(5).toString());
        BingoBoard bingoBoard = new BingoBoard(5);//빙고 선언하고 빙고 초기화까지
        System.out.println(bingoBoard.toString());
    }
}

