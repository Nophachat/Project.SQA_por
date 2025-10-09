package test;
import org.junit.jupiter.api.Test;

import Game.Brick;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BrickTest {

    // ทดสอบสถานะการถูกทำลายและ getRect()
    @Test
    public void testDestroyedAndGetRect() {
        Brick br = new Brick(10, 20, 50, 12);
        assertFalse(br.isDestroyed(), "เริ่มต้น brick ต้องไม่ถูกทำลาย");
        br.setDestroyed(true);
        assertTrue(br.isDestroyed(), "หลัง setDestroyed(true) ต้องเป็น destroyed");

        Rectangle r = br.getRect();
        assertEquals(10, r.x);
        assertEquals(20, r.y);
        assertEquals(50, r.width);
        assertEquals(12, r.height);
    }

    // ทดสอบ draw() (ไม่โยน exception)
    @Test
    public void testDraw() {
        Brick br = new Brick(5, 6, 40, 12);
        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        br.draw(g); // ไม่ควรโยน exception
    }
}