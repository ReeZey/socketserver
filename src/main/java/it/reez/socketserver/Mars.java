package it.reez.socketserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Mars {

    private static String[][] board = new String [100][100];

    static void load(){
        try {
            FileReader in = new FileReader( "map.txt");
            BufferedReader br = new BufferedReader(in);

            for (int r = 0; r<board.length;r++){
                String row = br.readLine();
                for (int c = 0; c <board.length;c++){
                    board[r][c] = String.valueOf(row.charAt(c));
                }
            }
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    static String get(int y, int x){
        return board[y][x];
    }
}