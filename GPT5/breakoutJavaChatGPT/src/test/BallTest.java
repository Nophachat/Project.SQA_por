package test;
import org.junit.jupiter.api.Test;

import Game.Ball;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BallTest {

    // ทดสอบ move() และ getters/setters
    @Test
    public void testMoveAndGettersSetters() {
        Ball b = new Ball(50, 60, 10, 2, 3);
        b.move();
        assertEquals(52, b.getX(), "x ต้องเพิ่มด้วย dx");
        assertEquals(63, b.getY(), "y ต้องเพิ่มด้วย dy");

        b.setX(10);
        b.setY(20);
        b.setDx(-1);
        b.setDy(-2);
        assertEquals(10, b.getX());
        assertEquals(20, b.getY());
        assertEquals(-1, b.getDx());
        assertEquals(-2, b.getDy());
    }

    // ทดสอบ getRect() และ draw()
    @Test
    public void testGetRectAndDraw() {
        Ball b = new Ball(30, 40, 12, 1, 1);
        Rectangle r = b.getRect();
        assertEquals(30, r.x);
        assertEquals(40, r.y);
        assertEquals(12, r.width);
        assertEquals(12, r.height);

        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        b.draw(g); // ไม่ควรโยน exception
    }
}
