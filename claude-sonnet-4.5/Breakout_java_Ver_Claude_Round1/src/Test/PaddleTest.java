package Test;

import Game.Paddle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ทดสอบคลาส Paddle - ครอบคลุมการเคลื่อนที่และการจำกัดขอบเขต
 */
public class PaddleTest {
    private Paddle paddle;
    private static final int SCREEN_WIDTH = 300;
    
    @BeforeEach
    public void setUp() {
        paddle = new Paddle(115, 350);
    }
    
    @Test
    public void testInitialPosition() {
        // ทดสอบตำแหน่งเริ่มต้น
        assertEquals(115, paddle.getX());
        assertEquals(350, paddle.getY());
    }
    
    @Test
    public void testGetDimensions() {
        // ทดสอบขนาดของ paddle
        assertEquals(70, paddle.getWidth());
        assertEquals(10, paddle.getHeight());
    }
    
    @Test
    public void testMoveLeft_Normal() {
        // ทดสอบการเคลื่อนที่ไปทางซ้ายปกติ (ไม่ถึงขอบ)
        int initialX = paddle.getX();
        paddle.moveLeft(SCREEN_WIDTH);
        assertTrue(paddle.getX() < initialX);
        assertTrue(paddle.getX() >= 0);
    }
    
    @Test
    public void testMoveLeft_HitBoundary() {
        // ทดสอบการเคลื่อนที่ซ้ายจนชนขอบ (x < 0, ต้องถูกจำกัดเป็น 0)
        paddle = new Paddle(5, 350);
        paddle.moveLeft(SCREEN_WIDTH);
        assertEquals(0, paddle.getX());
    }
    
    @Test
    public void testMoveLeft_AlreadyAtBoundary() {
        // ทดสอบการเคลื่อนที่เมื่ออยู่ที่ขอบซ้ายแล้ว
        paddle = new Paddle(0, 350);
        paddle.moveLeft(SCREEN_WIDTH);
        assertEquals(0, paddle.getX());
    }
    
    @Test
    public void testMoveRight_Normal() {
        // ทดสอบการเคลื่อนที่ไปทางขวาปกติ (ไม่ถึงขอบ)
        int initialX = paddle.getX();
        paddle.moveRight(SCREEN_WIDTH);
        assertTrue(paddle.getX() > initialX);
    }
    
    @Test
    public void testMoveRight_HitBoundary() {
        // ทดสอบการเคลื่อนที่ขวาจนชนขอบ (x > screenWidth - WIDTH)
        paddle = new Paddle(225, 350);
        paddle.moveRight(SCREEN_WIDTH);
        assertEquals(SCREEN_WIDTH - paddle.getWidth(), paddle.getX());
    }
    
    @Test
    public void testMoveRight_AlreadyAtBoundary() {
        // ทดสอบการเคลื่อนที่เมื่ออยู่ที่ขอบขวาแล้ว
        paddle = new Paddle(SCREEN_WIDTH - paddle.getWidth(), 350);
        paddle.moveRight(SCREEN_WIDTH);
        assertEquals(SCREEN_WIDTH - paddle.getWidth(), paddle.getX());
    }
    
    @Test
    public void testDraw() {
        // ทดสอบว่าเมธอด draw ไม่ throw exception
        assertDoesNotThrow(() -> paddle.draw(new java.awt.image.BufferedImage(300, 400, 
            java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics()));
    }
}