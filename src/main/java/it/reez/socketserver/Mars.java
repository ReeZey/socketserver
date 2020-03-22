package it.reez.socketserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static it.reez.socketserver.Main.players;

class Mars {

    private static String[][] board = new String [100][100];

    static void load(){
        try {
            //Load file
            FileReader in = new FileReader( "map.txt");
            BufferedReader br = new BufferedReader(in);

            //read each row
            for (int r = 0; r<board.length;r++){
                String row = br.readLine();
                //read each character
                for (int c = 0; c <board.length;c++){
                    board[r][c] = String.valueOf(row.charAt(c));
                }
            }
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    //get block from y x
    static String get(int y, int x){
        for(Rover r : players.values()){
            if(r.getX() == x && r.getY() == y)
                return "R";
        }
        return board[y][x];
    }

    //get block from int array
    static String get(int[]coords){
        for(Rover r : players.values()){
            if(r.getX() == coords[1] && r.getY() == coords[0])
                return "R";
        }
        return board[coords[0]][coords[1]];
    }
}
