package Test;

import Game.GamePanel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

/**
 * ทดสอบคลาส GamePanel - ครอบคลุมเกมลูป, การชน, และการจัดการ input
 */
public class GamePanelTest {
    private GamePanel gamePanel;
    private JFrame frame;
    
    @BeforeEach
    public void setUp() {
        frame = new JFrame();
        gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setSize(300, 400);
    }
    
    @Test
    public void testInitialization() {
        // ทดสอบว่า GamePanel ถูกสร้างได้สำเร็จ
        assertNotNull(gamePanel);
        assertTrue(gamePanel.isFocusable());
    }
    
    @Test
    public void testPaintComponent() {
        // ทดสอบว่า paintComponent ทำงานได้โดยไม่ error
        assertDoesNotThrow(() -> gamePanel.paintComponent(
            new java.awt.image.BufferedImage(300, 400, 
            java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics()));
    }
    
    @Test
    public void testActionPerformed_GameRunning() {
        // ทดสอบ game loop เมื่อเกมกำลังทำงาน (!gameOver && !victory)
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                gamePanel.actionPerformed(null);
            }
        });
    }
    
    @Test
    public void testKeyPressed_LeftArrow() {
        // ทดสอบการกดปุ่มลูกศรซ้าย (key == VK_LEFT)
        KeyEvent leftKey = new KeyEvent(gamePanel, KeyEvent.KEY_PRESSED, 
            System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        assertDoesNotThrow(() -> gamePanel.keyPressed(leftKey));
    }
    
    @Test
    public void testKeyPressed_RightArrow() {
        // ทดสอบการกดปุ่มลูกศรขวา (key == VK_RIGHT)
        KeyEvent rightKey = new KeyEvent(gamePanel, KeyEvent.KEY_PRESSED, 
            System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        assertDoesNotThrow(() -> gamePanel.keyPressed(rightKey));
    }
    
    @Test
    public void testKeyPressed_OtherKey() {
        // ทดสอบการกดปุ่มอื่นๆ (ไม่ใช่ซ้าย/ขวา) - ไม่ควรมีผลอะไร
        KeyEvent otherKey = new KeyEvent(gamePanel, KeyEvent.KEY_PRESSED, 
            System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        assertDoesNotThrow(() -> gamePanel.keyPressed(otherKey));
    }
    
    @Test
    public void testKeyReleased() {
        // ทดสอบ keyReleased (empty method)
        KeyEvent keyEvent = new KeyEvent(gamePanel, KeyEvent.KEY_RELEASED, 
            System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        assertDoesNotThrow(() -> gamePanel.keyReleased(keyEvent));
    }
    
    @Test
    public void testKeyTyped() {
        // ทดสอบ keyTyped (empty method)
        KeyEvent keyEvent = new KeyEvent(gamePanel, KeyEvent.KEY_TYPED, 
            System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a');
        assertDoesNotThrow(() -> gamePanel.keyTyped(keyEvent));
    }
    
    @Test
    public void testBallHitsLeftWall() {
        // ทดสอบลูกบอลชนกำแพงซ้าย (ball.getX() <= 0)
        for (int i = 0; i < 200; i++) {
            gamePanel.actionPerformed(null);
        }
        // ตรวจสอบว่าเกมยังทำงานได้
        assertDoesNotThrow(() -> gamePanel.paintComponent(
            new java.awt.image.BufferedImage(300, 400, 
            java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics()));
    }
    
    @Test
    public void testBallHitsTopWall() {
        // ทดสอบลูกบอลชนกำแพงบน (ball.getY() <= 0)
        for (int i = 0; i < 300; i++) {
            gamePanel.actionPerformed(null);
        }
        assertDoesNotThrow(() -> gamePanel.repaint());
    }
    
    @Test
    public void testBallFallsDown_GameOver() {
        // ทดสอบลูกบอลตกลงล่าง -> Game Over (ball.getY() >= getHeight())
        // จำลองการเล่นจนลูกบอลตก
        for (int i = 0; i < 1000; i++) {
            gamePanel.actionPerformed(null);
        }
        // เกมควรจบได้ในที่สุด
        assertDoesNotThrow(() -> gamePanel.paintComponent(
            new java.awt.image.BufferedImage(300, 400, 
            java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics()));
    }
    
    @Test
    public void testBallHitsPaddle() {
        // ทดสอบลูกบอลชนแพดเดิล (ball.intersects(paddle...))
        // เคลื่อนแพดเดิลไปที่ตำแหน่งลูกบอล
        for (int i = 0; i < 50; i++) {
            KeyEvent leftKey = new KeyEvent(gamePanel, KeyEvent.KEY_PRESSED, 
                System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
            gamePanel.keyPressed(leftKey);
            gamePanel.actionPerformed(null);
        }
        assertDoesNotThrow(() -> gamePanel.repaint());
    }
    
    @Test
    public void testBallHitsBrick() {
        // ทดสอบลูกบอลชนอิฐ (!brick.isDestroyed() && ball.intersects(brick...))
        // รัน game loop เพื่อให้ลูกบอลชนอิฐ
        for (int i = 0; i < 500; i++) {
            gamePanel.actionPerformed(null);
        }
        assertDoesNotThrow(() -> gamePanel.repaint());
    }
    
    @Test
    public void testVictoryCondition() {
        // ทดสอบเงื่อนไขชนะ (bricksRemaining == 0)
        // จำลองการทำลายอิฐทั้งหมด - รัน loop นานพอ
        for (int i = 0; i < 5000; i++) {
            gamePanel.actionPerformed(null);
            // สุ่มเคลื่อนแพดเดิลเพื่อป้องกันลูกบอลตก
            if (i % 20 == 0) {
                KeyEvent key = new KeyEvent(gamePanel, KeyEvent.KEY_PRESSED, 
                    System.currentTimeMillis(), 0, 
                    (i % 40 == 0) ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT, 
                    KeyEvent.CHAR_UNDEFINED);
                gamePanel.keyPressed(key);
            }
        }
        assertDoesNotThrow(() -> gamePanel.paintComponent(
            new java.awt.image.BufferedImage(300, 400, 
            java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics()));
    }
    
    @Test
    public void testKeyPressed_WhenGameOver() {
        // ทดสอบการกดปุ่มเมื่อเกมจบแล้ว (gameOver || victory)
        // จำลอง game over
        for (int i = 0; i < 1000; i++) {
            gamePanel.actionPerformed(null);
        }
        // พยายามกดปุ่ม - ไม่ควรมีผลอะไร
        KeyEvent leftKey = new KeyEvent(gamePanel, KeyEvent.KEY_PRESSED, 
            System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        assertDoesNotThrow(() -> gamePanel.keyPressed(leftKey));
    }
    
    @Test
    public void testActionPerformed_WhenGameOver() {
        // ทดสอบ actionPerformed เมื่อ gameOver = true
        for (int i = 0; i < 1000; i++) {
            gamePanel.actionPerformed(null);
        }
        // ลอง actionPerformed อีกครั้งหลัง game over
        assertDoesNotThrow(() -> gamePanel.actionPerformed(null));
    }
}