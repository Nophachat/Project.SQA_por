package Game;

import java.awt.Color;
import java.awt.Graphics;

public class Paddle {
    private int x, y;
    private static final int WIDTH = 70;
    private static final int HEIGHT = 10;
    private static final int SPEED = 15;
    
    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void moveLeft(int screenWidth) {
        x -= SPEED;
        if (x < 0) {
            x = 0;
        }
    }
    
    public void moveRight(int screenWidth) {
        x += SPEED;
        if (x > screenWidth - WIDTH) {
            x = screenWidth - WIDTH;
        }
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return WIDTH; }
    public int getHeight() { return HEIGHT; }
}