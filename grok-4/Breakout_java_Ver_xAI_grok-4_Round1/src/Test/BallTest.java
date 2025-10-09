package Test;

import Game.Ball;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BallTest {
    @Test
    // ทดสอบคอนสตรัคเตอร์ของ Ball เพื่อให้แน่ใจว่าตำแหน่ง ขนาด และความเร็วถูกตั้งค่าอย่างถูกต้อง
    void testConstructor() {
        Ball ball = new Ball(50, 60, 20, 20, 2, -2);
        assertEquals(50, ball.getX());
        assertEquals(60, ball.getY());
        assertEquals(20, ball.getWidth());
        assertEquals(20, ball.getHeight());
    }

    @Test
    // ทดสอบการเคลื่อนที่ของ Ball (move) โดยตรวจสอบตำแหน่งใหม่
    void testMove() {
        Ball ball = new Ball(50, 60, 20, 20, 2, -2);
        ball.move();
        assertEquals(52, ball.getX());
        assertEquals(58, ball.getY());
    }

    @Test
    // ทดสอบการกลับทิศทางความเร็วในแนว x (reverseDx)
    void testReverseDx() {
        Ball ball = new Ball(50, 60, 20, 20, 2, -2);
        ball.reverseDx();
        ball.move();
        assertEquals(48, ball.getX()); // เคลื่อนไปทางซ้าย
    }

    @Test
    // ทดสอบการกลับทิศทางความเร็วในแนว y (reverseDy)
    void testReverseDy() {
        Ball ball = new Ball(50, 60, 20, 20, 2, -2);
        ball.reverseDy();
        ball.move();
        assertEquals(62, ball.getY()); // เคลื่อนลง
    }

    @Test
    // ทดสอบ intersects: กรณีชน (true) และไม่ชน (false) เพื่อครอบคลุม branch ทั้งหมด
    void testIntersects() {
        Ball ball = new Ball(50, 50, 20, 20, 0, 0);
        assertTrue(ball.intersects(60, 60, 10, 10)); // ชน
        assertFalse(ball.intersects(0, 0, 10, 10)); // ไม่ชน
        assertFalse(ball.intersects(100, 100, 10, 10)); // ไม่ชน (branch coverage สำหรับ && conditions)
    }
}