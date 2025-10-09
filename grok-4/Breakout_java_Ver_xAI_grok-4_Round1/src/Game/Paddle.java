package Game;

import java.awt.event.KeyEvent;

public class Paddle {
    private int x, y, width, height;
    private static final int SPEED = 5;
    private static final int GAME_WIDTH = 300;

    public Paddle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void move(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT && x > 0) {
            x -= SPEED;
        } else if (keyCode == KeyEvent.VK_RIGHT && x < GAME_WIDTH - width) {
            x += SPEED;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}