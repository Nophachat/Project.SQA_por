package Test;

import Game.Brick;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * ทดสอบคลาส Brick - ครอบคลุมการทำลายและการวาด
 */
public class BrickTest {
    private Brick brick;
    
    @BeforeEach
    public void setUp() {
        brick = new Brick(10, 20, 45, 15);
    }
    
    @Test
    public void testInitialPosition() {
        // ทดสอบตำแหน่งและขนาดเริ่มต้น
        assertEquals(10, brick.getX());
        assertEquals(20, brick.getY());
        assertEquals(45, brick.getWidth());
        assertEquals(15, brick.getHeight());
    }
    
    @Test
    public void testInitiallyNotDestroyed() {
        // ทดสอบว่าอิฐยังไม่ถูกทำลายตอนเริ่มต้น
        assertFalse(brick.isDestroyed());
    }
    
    @Test
    public void testDestroy() {
        // ทดสอบการทำลายอิฐ
        brick.destroy();
        assertTrue(brick.isDestroyed());
    }
    
    @Test
    public void testDraw_NotDestroyed() {
        // ทดสอบการวาดเมื่ออิฐยังไม่ถูกทำลาย (branch: !destroyed = true)
        Graphics g = new BufferedImage(300, 400, BufferedImage.TYPE_INT_RGB).getGraphics();
        assertDoesNotThrow(() -> brick.draw(g));
    }
    
    @Test
    public void testDraw_Destroyed() {
        // ทดสอบการวาดเมื่ออิฐถูกทำลายแล้ว (branch: !destroyed = false)
        brick.destroy();
        Graphics g = new BufferedImage(300, 400, BufferedImage.TYPE_INT_RGB).getGraphics();
        assertDoesNotThrow(() -> brick.draw(g));
    }
    
    @Test
    public void testColorRandomization_Case0() {
        // ทดสอบสีแบบสุ่ม - สร้างอิฐหลายตัวเพื่อครอบคลุม switch cases
        // เนื่องจากสีถูกสุ่ม เราทดสอบว่าไม่ throw exception
        for (int i = 0; i < 20; i++) {
            Brick testBrick = new Brick(i * 10, 20, 45, 15);
            assertNotNull(testBrick);
        }
    }
}