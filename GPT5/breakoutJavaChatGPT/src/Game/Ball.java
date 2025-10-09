package Game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Ball {
    private int x, y, diameter;
    private int dx, dy;

    public Ball(int x, int y, int diameter, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, diameter, diameter);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    // getters and setters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDiameter() { return diameter; }
    public int getDx() { return dx; }
    public int getDy() { return dy; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setDx(int dx) { this.dx = dx; }
    public void setDy(int dy) { this.dy = dy; }
}