package Game;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Brick {
    private int x, y, width, height;
    private boolean destroyed = false;
    private Color color;

    public Brick(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // กำหนดสีแบบง่าย ๆ สลับกันตามตำแหน่งเพื่อความสวยงาม
        this.color = Color.ORANGE;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean val) { destroyed = val; }
}