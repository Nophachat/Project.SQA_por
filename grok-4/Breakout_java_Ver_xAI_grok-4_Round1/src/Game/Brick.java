package Game;

public class Brick {
    private int x, y, width, height;
    private boolean destroyed = false;

    public Brick(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void destroy() { destroyed = true; }
    public boolean isDestroyed() { return destroyed; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}