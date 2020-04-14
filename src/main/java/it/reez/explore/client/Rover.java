package it.reez.explore.client;

import it.reez.explore.Main;
import it.reez.explore.io.World;

import static it.reez.explore.Main.*;
import static it.reez.explore.Values.*;

public class Rover {
    private final String password;
    private int x = mapWidth/2, y = mapHeight/2, r = 0;
    private boolean online = false;

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
        return Main.getPlayers().get(name);
    }

    public boolean getOnline(){
        return online;
    }

    public void setOnline(){
        online = !online;
    }

    public void forward() {
        if(!blocked()){
            if(r == NORTH){
                y--;
            }else if(r == EAST){
                x++;
            }else if(r == SOUTH){
                y++;
            }else if(r == WEST){
                x--;
            }
        }
    }

    public void rRight(){
        r++;
        if(r > WEST)
            r = 0;
    }

    public void rLeft(){
        r--;
        if(r < NORTH)
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
