package Game;

public class Ball {
    private int x, y, width, height, dx, dy;

    public Ball(int x, int y, int width, int height, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void reverseDx() { dx = -dx; }
    public void reverseDy() { dy = -dy; }

    public boolean intersects(int objX, int objY, int objWidth, int objHeight) {
        return x < objX + objWidth && x + width > objX &&
               y < objY + objHeight && y + height > objY;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

	public Integer getDx() {
		return dx;
	}
	public Integer getDy() {
		return dy;
	}
	
}