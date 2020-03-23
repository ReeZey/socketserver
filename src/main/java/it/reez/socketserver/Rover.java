package it.reez.socketserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.reez.socketserver.Main.players;

class Rover{
    private int x;
    private int y;
    private int r;
    private String c;
    private int p = 0;
    private List<String> digged = new ArrayList<>();

    Rover(int x, int y, int r, String c) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.c = c;
    }

    //get rover x
    int getX() {
        return x;
    }

    //get rover y
    int getY() {
        return y;
    }

    //get rover rotation
    int getR() { return r; }

    //get points
    int getP() {
        return p;
    }


    //get rover color
    String getC(){
        return c;
    }

    //set rover x
    private void setX(int x) {
        this.x = x;
    }

    //set rover y
    private void setY(int y) {
        this.y = y;
    }

    //set points
    private void setP(int p) {
        this.p = p;
    }

    //rover turn left
    void rLeft(){
        if(this.r > 0){
            this.r--;
        }else{
            this.r = 3;
        }
    }

    //rover turn right
    void rRight(){
        if(this.r < 3){
            this.r++;
        }else{
            this.r = 0;
        }
    }

    //get block infront of player
    private int[] getBlock(){
        int[] block = new int[2];
        if (this.r == 0) {
            block = new int[]{getY() - 1, getX()};
        } else if (this.r == 1) {
            block = new int[]{getY(), getX() + 1};
        } else if (this.r == 2) {
            block = new int[]{getY() + 1, getX()};
        } else if (this.r == 3) {
            block = new int[]{getY(), getX() - 1};
        }
        return block;
    }


    //check for collision then move rover forward
    void forward(){
        if(this.r == 0){
            if(Mars.get(getBlock()).equals(".")){
                setY(getY()-1);
            }
        }else if(this.r == 1){
            if(Mars.get(getBlock()).equals(".")){
                setX(getX()+1);
            }
        }else if(this.r == 2){
            if(Mars.get(getBlock()).equals(".")){
                setY(getY()+1);
            }
        }else if(this.r == 3){
            if(Mars.get(getBlock()).equals(".")){
                setX(getX()-1);
            }
        }
    }

    //rover scan function
    String scan(int x, int y, int r){
        StringBuilder scan = new StringBuilder();
        if(r == 0){
            for(int i = -2; i<3; i++){
                scan.append(Mars.get(y - 2, x + i));
            }
            for(int i = -1; i<2; i++){
                scan.append(Mars.get(y - 1, x + i));
            }
        }else if(r == 1){
            for(int i = -2; i<3; i++){
                scan.append(Mars.get(y + i, x + 2));
            }
            for(int i = -1; i<2; i++){
                scan.append(Mars.get(y + i, x + 1));
            }
        }else if(r == 2){
            for(int i = -2; i<3; i++){
                scan.append(Mars.get(y + 2, x - i));
            }
            for(int i = -1; i<2; i++){
                scan.append(Mars.get(y + 1, x - i));
            }
        }else if(r == 3){
            for(int i = -2; i<3; i++){
                scan.append(Mars.get(y - i, x - 2));
            }
            for(int i = -1; i<2; i++){
                scan.append(Mars.get(y - i, x - 1));
            }
        }
        return scan.toString();
    }

    //digging system
    void dig() {
        if(!this.digged.contains(Arrays.toString(getBlock()))) {
            String block = Mars.get(getBlock());
            if(block.equals("R")){
                for(Rover r : players.values()){
                    if(r.getX() == getBlock()[1] && r.getY() == getBlock()[0]){
                        r.setP(r.getP()-(r.getP()/8));
                        setP(getP()+r.getP()/8);
                    }
                }
            }else{
                switch (block) {
                    case "W":
                        setP(getP() + 200);
                        break;
                    case "o":
                        setP(getP() + 1);
                        break;
                    case "O":
                        setP(getP() + 10);
                        break;
                    case "S":
                        setP(getP() + 5000);
                        break;
                }
                this.digged.add(Arrays.toString(getBlock()));
            }
        }
    }

    //get direction
    String getDir(){
        String[] dir = {"N", "E", "S", "W"};
        return dir[getR()];
    }
}
