package Game;

import java.awt.Color;
import java.awt.Graphics;

public class Brick {
    private int x, y, width, height;
    private boolean destroyed = false;
    private Color color;
    
    public Brick(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        // สุ่มสีอิฐ
        int colorChoice = (int)(Math.random() * 5);
        switch(colorChoice) {
            case 0: color = Color.RED; break;
            case 1: color = Color.GREEN; break;
            case 2: color = Color.YELLOW; break;
            case 3: color = Color.MAGENTA; break;
            default: color = Color.CYAN; break;
        }
    }
    
    public void destroy() {
        destroyed = true;
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    public void draw(Graphics g) {
        if (!destroyed) {
            g.setColor(color);
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}