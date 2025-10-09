package TestRound3;

import Game.Paddle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.KeyEvent;

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
    // ทดสอบ move: ทางซ้ายปกติและขอบ (branch if left && x > 0)
    void testMoveLeft() {
        Paddle paddle = new Paddle(10, 200, 60, 10);
        paddle.move(KeyEvent.VK_LEFT);
        assertEquals(5, paddle.getX());
        paddle.move(KeyEvent.VK_LEFT);
        paddle.move(KeyEvent.VK_LEFT); // ถึงขอบ
        assertEquals(0, paddle.getX());
    }

    @Test
    // ทดสอบ move: ทางขวาปกติและขอบ (branch else if right && x < GAME_WIDTH - width)
    void testMoveRight() {
        Paddle paddle = new Paddle(200, 200, 60, 10);
        paddle.move(KeyEvent.VK_RIGHT);
        assertEquals(205, paddle.getX());
        while (paddle.getX() < 240) paddle.move(KeyEvent.VK_RIGHT);
        paddle.move(KeyEvent.VK_RIGHT); // ถึงขอบ
        assertEquals(240, paddle.getX());
    }

    @Test
    // ทดสอบ move: คีย์อื่น (ไม่เข้า branch ใดๆ)
    void testMoveInvalidKey() {
        Paddle paddle = new Paddle(100, 200, 60, 10);
        paddle.move(KeyEvent.VK_UP);
        assertEquals(100, paddle.getX());
    }
}