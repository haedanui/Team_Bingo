package com.example;

import java.util.HashSet;
import java.util.Set;

public class BingoBoard {
    private static final StringBuilder SB = new StringBuilder();
    private final int size; //static 을 선언하지 않으면 문제가 생기려나?(생길시 수정 바람)

    private final int[][] board;
    

    public BingoBoard(int size) {
        this.size = size;
        board = new int[size][size];
        initBoard();
    }

    private void initBoard() {
        Set<Integer> set = new HashSet<>(size * size);
        // ToDo size를 받아서 적당한 범위의 값을 책정해 줄 것!
        int range = 100;//범위
        int rand;
        for(int i = 0; i < size * size; i++){
            while (set.contains(rand = (int) (Math.random()*range))) {}// 값이 중복이면 아닌 값을 뽑을때 까지 반복

            set.add(rand);
        }

        int index = 0;
        for (int n: set){
            board[index/size][index%size] = n;
            index++;
        }
    }

    //TODO 빙고의 여부확인을 체크하는 함수 추가

    @Override
    public String toString() {
        SB.setLength(0); // init.

        for (int i = 0; i < size * size; ++i) {
            SB.append(String.format("[%2d]", board[i / size][i % size]));
            if ((i + 1) % size == 0)
                SB.append('\n');
        }

        return SB.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; ++i)
            System.out.println(new BingoBoard(5).toString());
    }
}
