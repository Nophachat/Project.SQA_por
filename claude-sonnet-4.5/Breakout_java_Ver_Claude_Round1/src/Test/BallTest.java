package Test;

import Game.Ball;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ทดสอบคลาส Ball - ครอบคลุมการเคลื่อนที่, การเด้ง, และการตรวจจับการชน
 */
public class BallTest {
    private Ball ball;
    
    @BeforeEach
    public void setUp() {
        ball = new Ball(100, 100);
    }
    
    @Test
    public void testInitialPosition() {
        // ทดสอบตำแหน่งเริ่มต้นของลูกบอล
        assertEquals(100, ball.getX());
        assertEquals(100, ball.getY());
    }
    
    @Test
    public void testGetSize() {
        // ทดสอบขนาดของลูกบอล
        assertEquals(10, ball.getSize());
    }
    
    @Test
    public void testMove() {
        // ทดสอบการเคลื่อนที่ของลูกบอลในทิศทางปกติ
        int initialX = ball.getX();
        int initialY = ball.getY();
        ball.move();
        assertNotEquals(initialX, ball.getX());
        assertNotEquals(initialY, ball.getY());
    }
    
    @Test
    public void testReverseX() {
        // ทดสอบการกลับทิศทางแกน X
        int x1 = ball.getX();
        ball.move();
        int x2 = ball.getX();
        int deltaX1 = x2 - x1;
        
        ball.reverseX();
        ball.move();
        int x3 = ball.getX();
        int deltaX2 = x3 - x2;
        
        assertEquals(-deltaX1, deltaX2);
    }
    
    @Test
    public void testReverseY() {
        // ทดสอบการกลับทิศทางแกน Y
        int y1 = ball.getY();
        ball.move();
        int y2 = ball.getY();
        int deltaY1 = y2 - y1;
        
        ball.reverseY();
        ball.move();
        int y3 = ball.getY();
        int deltaY2 = y3 - y2;
        
        assertEquals(-deltaY1, deltaY2);
    }
    
    @Test
    public void testIntersects_True() {
        // ทดสอบการชนกัน - กรณีชนจริง (ทุก branch ที่เป็น true)
        ball = new Ball(50, 50);
        assertTrue(ball.intersects(45, 45, 20, 20));
    }
    
    @Test
    public void testIntersects_False_Left() {
        // ทดสอบไม่ชน - ball อยู่ทางซ้ายของวัตถุ (x < objX + objWidth = false)
        ball = new Ball(10, 50);
        assertFalse(ball.intersects(100, 50, 20, 20));
    }
    
    @Test
    public void testIntersects_False_Right() {
        // ทดสอบไม่ชน - ball อยู่ทางขวาของวัตถุ (x + SIZE > objX = false)
        ball = new Ball(150, 50);
        assertFalse(ball.intersects(50, 50, 20, 20));
    }
    
    @Test
    public void testIntersects_False_Above() {
        // ทดสอบไม่ชน - ball อยู่ด้านบนของวัตถุ (y < objY + objHeight = false)
        ball = new Ball(50, 10);
        assertFalse(ball.intersects(50, 100, 20, 20));
    }
    
    @Test
    public void testIntersects_False_Below() {
        // ทดสอบไม่ชน - ball อยู่ด้านล่างของวัตถุ (y + SIZE > objY = false)
        ball = new Ball(50, 150);
        assertFalse(ball.intersects(50, 50, 20, 20));
    }
    
    @Test
    public void testDraw() {
        // ทดสอบว่าเมธอด draw ไม่ throw exception
        assertDoesNotThrow(() -> ball.draw(new java.awt.image.BufferedImage(300, 400, 
            java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics()));
    }
}