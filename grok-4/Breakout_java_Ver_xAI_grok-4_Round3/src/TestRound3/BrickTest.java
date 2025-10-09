package TestRound3;

import Game.Brick;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BrickTest {
    @Test
    // ทดสอบคอนสตรัคเตอร์ของ Brick เพื่อให้แน่ใจว่าตำแหน่งและขนาดถูกตั้งค่าอย่างถูกต้อง และไม่ถูกทำลายเริ่มต้น
    void testConstructor() {
        Brick brick = new Brick(10, 20, 50, 20);
        assertEquals(10, brick.getX());
        assertEquals(20, brick.getY());
        assertEquals(50, brick.getWidth());
        assertEquals(20, brick.getHeight());
        assertFalse(brick.isDestroyed());
    }

    @Test
    // ทดสอบการทำลาย Brick (destroy) และตรวจสอบสถานะ isDestroyed (branch: ก่อนและหลัง destroy)
    void testDestroy() {
        Brick brick = new Brick(10, 20, 50, 20);
        assertFalse(brick.isDestroyed()); // ก่อน
        brick.destroy();
        assertTrue(brick.isDestroyed()); // หลัง
    }
}