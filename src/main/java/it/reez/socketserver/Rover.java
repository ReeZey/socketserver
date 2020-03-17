package it.reez.socketserver;

class Rover {
    private int x;
    private int y;
    private int r;

    Rover(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getR() {
        return r;
    }

    private void setX(int x) {
        this.x = x;
    }

    private void setY(int y) {
        this.y = y;
    }

    void rLeft(){
        if(this.r > 0){
            this.r--;
        }else{
            this.r = 3;
        }
    }

    void rRight(){
        if(this.r < 3){
            this.r++;
        }else{
            this.r = 0;
        }
    }

    void forward(){
        if(this.r == 0){
            if(Mars.get(getY()-1,getX()).equals(".")){
                setY(getY()-1);
            }
        }else if(this.r == 1){
            if(Mars.get(getY(), getX()+1).equals(".")){
                setX(getX()+1);
            }
        }else if(this.r == 2){
            if(Mars.get(getY()+1, getX()).equals(".")){
                setY(getY()+1);
            }
        }else if(this.r == 3){
            if(Mars.get(getY(), getX()-1).equals(".")){
                setX(getX()-1);
            }
        }
    }

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
                scan.append(Mars.get(y + 2, x + i));
            }
            for(int i = -1; i<2; i++){
                scan.append(Mars.get(y + 1, x + i));
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

}
