package Game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Paddle {
    private int x, y, width, height;
    private int panelWidth;
    private int speed = 5;

    public Paddle(int x, int y, int width, int height, int panelWidth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.panelWidth = panelWidth;
    }

    public void moveLeft() {
        x -= speed;
        if (x < 0) x = 0;
    }

    public void moveRight() {
        x += speed;
        if (x + width > panelWidth) x = panelWidth - width;
    }

    public void checkBounds() {
        if (x < 0) x = 0;
        if (x + width > panelWidth) x = panelWidth - width;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    // getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
}