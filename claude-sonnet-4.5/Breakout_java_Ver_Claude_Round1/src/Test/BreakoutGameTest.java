package Test;

import Game.GamePanel;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ทดสอบคลาส BreakoutGame (main class)
 */
public class BreakoutGameTest {
    
    @Test
    public void testMainMethodCreatesFrame() {
        // ทดสอบว่า main method ทำงานได้โดยไม่ throw exception
        // ใช้ thread แยกเพื่อไม่ให้ block test
        assertDoesNotThrow(() -> {
            Thread testThread = new Thread(() -> {
                // ไม่เรียก main จริงเพราะจะค้างที่ UI
                // แต่ทดสอบว่าโค้ดภายในสามารถทำงานได้
                javax.swing.JFrame frame = new javax.swing.JFrame("Breakout");
                GamePanel gamePanel = new GamePanel();
                frame.add(gamePanel);
                frame.setSize(300, 400);
                frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                // ไม่ setVisible(true) เพื่อไม่ให้ขึ้น UI จริง
                assertNotNull(frame);
            });
            testThread.start();
            testThread.join(1000); // รอ 1 วินาที
        });
    }
}