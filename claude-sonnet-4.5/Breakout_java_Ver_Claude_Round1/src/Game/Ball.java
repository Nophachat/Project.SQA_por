package Game;

import java.awt.Color;
import java.awt.Graphics;

public class Ball {
    private int x, y;
    private int dx = 2, dy = -2;
    private static final int SIZE = 10;
    
    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void move() {
        x += dx;
        y += dy;
    }
    
    public void reverseX() {
        dx = -dx;
    }
    
    public void reverseY() {
        dy = -dy;
    }
    
    public boolean intersects(int objX, int objY, int objWidth, int objHeight) {
        return x < objX + objWidth && 
               x + SIZE > objX && 
               y < objY + objHeight && 
               y + SIZE > objY;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, SIZE, SIZE);
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return SIZE; }
}