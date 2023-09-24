package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lombok.Getter;

@Getter
public class BingoBoard {
    private static final int DEFAULT_SIZE = 5;
    private static final StringBuilder SB = new StringBuilder();

    private int turn;
    private String[][] board;

    public BingoBoard() {
        this(DEFAULT_SIZE);
    }
    
    public BingoBoard(int boardSize) {
        if (boardSize <= 0) throw new IllegalArgumentException();
        
        board = new String[boardSize][boardSize];
        initBoard();
    }

    public int getSize() { // 
        assert (board.length == board[0].length);

        return board.length;
    }

    private void initBoard() {
        HashSet<Integer> set = new HashSet<>(getSize() * getSize());
        List<Integer> list = new ArrayList<>(getSize() * getSize());

        int range = getSize() * getSize(); // range : 1 ~ 25.
        int rand;
        for (int i = 0; i < range; ++i) {
            while (set.contains(rand = (int)((Math.random() * range) + 1))) {
                // 값이 중복이면 아닌 값을 뽑을때 까지 반복
            }

            set.add(rand);
            list.add(rand);
        }

        int index = 0;
        for (int n : list) {
            board[index / getSize()][index % getSize()] = String.valueOf(n);
            index++;
        }
    }

    public boolean canPlace(int index) {
        if (index < 0 || index >= getSize() * getSize()) return false;

        int i = index / getSize();
        int j = index % getSize();

        try { // board[i][j]부분에 다른 값이 채워져있는지 확인.

            Integer.parseInt(board[i][j]);

            return true;
            
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public synchronized boolean tryPlace(final int index, final String mark) {
        if (!canPlace(index)) return false;

        board[index / getSize()][index % getSize()] = mark;
        turn++;

        return true;
    }

    public boolean isDone() {
        for (int i = 0; i < getSize() * getSize(); ++i) if (canPlace(i)) return false;

        return true;
    }

    public boolean checkBingo() {
        int len = getSize();

        // 가로 빙고 확인
        for (int i = 0 ; i < len; i++) {
            int count = 0;

            String temp = board[i][0];
            for(int j = 0; j < len ; j++) {
                if (board[i][j].equals(temp)) count++;
            }

            if (count == len) return true;
        }

        // 세로 빙고 확인
        for (int i = 0 ; i < len; i++) {
            int count = 0;

            String temp = board[0][i];
            for(int j = 0; j < len ; j++) {
                if (board[j][i].equals(temp)) count++;
            }

            if (count == len) return true;
        }

        // 11시에서 5시로 가는 대각선
        for (int i = 0, j = 0; i < len; i++, j++) {
            int count = 0;

            String temp = board[i][j];
            if (board[i][j].equals(temp)) {
                count++;
            }

            if (count == len) return true;
        }

        // 1시에서 7시로 가는 대각선
        for (int i = 0, j = len - 1; i < len; i++, j--) {
            int count = 0;

            String temp = board[i][j];
            if (board[i][j].equals(temp)) {
                count++;
            }

            if (count == len) return true;
        }

        return false;
    }
    
    @Override
    public String toString() {
        SB.setLength(0); // init.

        int range = getSize() * getSize();
        for (int i = 0; i < range; ++i) {
            SB.append(String.format("[%2s]", board[i / getSize()][i % getSize()]));

            if ((i + 1) % getSize() == 0) SB.append(System.lineSeparator());
        }

        SB.append("========================================");
        SB.append(System.lineSeparator());

        return SB.toString();
    }

    public static void main(String[] args) {
        BingoBoard bingoBoard = new BingoBoard();
        System.out.println(bingoBoard.toString());

        bingoBoard.tryPlace(15, "x");

        System.out.println(bingoBoard.toString());
    }
}

