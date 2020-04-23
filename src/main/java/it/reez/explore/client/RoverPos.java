package it.reez.explore.client;

public class RoverPos {
    public int x, y, r;

    public RoverPos(Rover r) {
        this.x = r.getX();
        this.y = r.getY();
        this.r = r.getR();
    }
}
