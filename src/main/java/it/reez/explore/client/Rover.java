package it.reez.explore.client;

import it.reez.explore.Main;
import it.reez.explore.io.World;

import static it.reez.explore.Values.*;

public class Rover {
    private final String password;
    private int x = 0, y = 0, r = 0;

    public Rover(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public static Rover get(String name){
        return Main.players.get(name);
    }

    public void forward() {
        if(!blocked()){
            if(r == 0){
                y--;
            }else if(r == 1){
                x++;
            }else if(r == 2){
                y++;
            }else if(r == 3){
                x--;
            }
        }
    }

    public void rRight(){
        r++;
        if(r > 3)
            r = 0;
    }
    public void rLeft(){
        r--;
        if(r < 0)
            r = 3;
    }

    public String getPos() {
        String[] dir = {"N","E","S","W"};
        return "Position x:"+ x + " y:" + y + " r:" + dir[r];
    }

    public String scan(){
        StringBuilder scan = new StringBuilder();
        if(r == NORTH){
            for(int i = -2; i<3; i++){
                scan.append(World.get(y - 2, x + i));
            }
            for(int i = -1; i<2; i++){
                scan.append(World.get(y - 1, x + i));
            }
        }else if(r == EAST){
            for(int i = -2; i<3; i++){
                scan.append(World.get(y + i, x + 2));
            }
            for(int i = -1; i<2; i++){
                scan.append(World.get(y + i, x + 1));
            }
        }else if(r == SOUTH){
            for(int i = -2; i<3; i++){
                scan.append(World.get(y + 2, x - i));
            }
            for(int i = -1; i<2; i++){
                scan.append(World.get(y + 1, x - i));
            }
        }else if(r == WEST){
            for(int i = -2; i<3; i++){
                scan.append(World.get(y - i, x - 2));
            }
            for(int i = -1; i<2; i++){
                scan.append(World.get(y - i, x - 1));
            }
        }
        return scan.toString();
    }

    public Boolean blocked(){
        String block = ".";
        if(r == NORTH){
            block = World.get(y-1, x);
        }else if(r == EAST){
            block = World.get(y, x+1);
        }else if(r == SOUTH){
            block = World.get(y+1, x);
        }else if(r == WEST){
            block = World.get(y, x-1);
        }
        return block.equals(STONE) || block.equals(WATER);
    }
}
