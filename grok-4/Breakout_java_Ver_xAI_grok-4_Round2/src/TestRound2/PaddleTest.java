package TestRound2;

import Game.Paddle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaddleTest {
    @Test
    // ทดสอบคอนสตรัคเตอร์ของ Paddle เพื่อให้แน่ใจว่าตำแหน่งและขนาดถูกตั้งค่าอย่างถูกต้อง
    void testConstructor() {
        Paddle paddle = new Paddle(100, 200, 60, 10);
        assertEquals(100, paddle.getX());
        assertEquals(200, paddle.getY());
        assertEquals(60, paddle.getWidth());
        assertEquals(10, paddle.getHeight());
    }

    @Test
    // ทดสอบการเคลื่อนที่ไปทางซ้ายปกติและที่ขอบ (branch: if key left && x > 0)
    void testMoveLeft() {
        Paddle paddle = new Paddle(10, 200, 60, 10);
        paddle.move(java.awt.event.KeyEvent.VK_LEFT); // เคลื่อนปกติ
        assertEquals(5, paddle.getX());
        paddle.move(java.awt.event.KeyEvent.VK_LEFT); // เคลื่อนจน x <= 0 (ไม่เคลื่อน)
        paddle.move(java.awt.event.KeyEvent.VK_LEFT);
        assertEquals(0, paddle.getX());
    }

    @Test
    // ทดสอบการเคลื่อนที่ไปทางขวาปกติและที่ขอบ (branch: if key right && x < GAME_WIDTH - width)
    void testMoveRight() {
        Paddle paddle = new Paddle(200, 200, 60, 10);
        paddle.move(java.awt.event.KeyEvent.VK_RIGHT); // เคลื่อนปกติ
        assertEquals(205, paddle.getX());
        while (paddle.getX() < 300 - 60) {
            paddle.move(java.awt.event.KeyEvent.VK_RIGHT);
        }
        paddle.move(java.awt.event.KeyEvent.VK_RIGHT); // ถึงขอบ (ไม่เคลื่อน)
        assertEquals(240, paddle.getX());
    }

    @Test
    // ทดสอบการเคลื่อนที่ด้วยคีย์อื่น (branch: else - ไม่เคลื่อน)
    void testMoveInvalidKey() {
        Paddle paddle = new Paddle(100, 200, 60, 10);
        paddle.move(java.awt.event.KeyEvent.VK_UP); // คีย์ไม่ใช่ left/right
        assertEquals(100, paddle.getX());
    }
}