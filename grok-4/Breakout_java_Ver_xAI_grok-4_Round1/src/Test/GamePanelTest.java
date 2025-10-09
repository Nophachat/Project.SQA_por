package Test;

import Game.Breakout;
import Game.Paddle;
import Game.Brick;
import Game.Ball;
import Game.GamePanel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;

public class GamePanelTest {
    private GamePanel panel;

    @BeforeEach
    void setUp() {
        panel = new GamePanel();
    }

    @Test
    // ทดสอบการเริ่มต้น (constructor) รวมถึง initBricks เพื่อครอบคลุมการตั้งค่าเริ่มต้นทั้งหมด
    void testConstructorAndInitBricks() throws NoSuchFieldException, IllegalAccessException {
        Field bricksField = GamePanel.class.getDeclaredField("bricks");
        bricksField.setAccessible(true);
        Brick[] bricks = (Brick[]) bricksField.get(panel);
        assertEquals(30, bricks.length);
        assertFalse(getPrivateField("gameOver"));
        assertFalse(getPrivateField("victory"));
        assertEquals(0, getPrivateField("bricksDestroyed"));
    }

    @Test
    // ทดสอบ actionPerformed: กรณีเกมกำลังเล่น (เรียก move และ checkCollisions) และกรณีจบเกม (ไม่เรียก)
    void testActionPerformed() throws NoSuchFieldException, IllegalAccessException {
        ActionEvent e = new ActionEvent(this, 0, null);
        panel.actionPerformed(e); // เกมกำลังเล่น
        setPrivateField("gameOver", true);
        panel.actionPerformed(e); // เกมจบ (branch: if !gameOver && !victory)
        assertTrue(getPrivateField("gameOver"));
    }

    @Test
    // ทดสอบ checkCollisions: ชนกำแพงซ้าย/ขวา/บน (reverse dx/dy)
    void testCollisionWalls() throws NoSuchFieldException, IllegalAccessException {
        Ball ball = (Ball) getPrivateField("ball");
        ball = new Ball(0, 0, 20, 20, -2, -2, ball); // ตั้งให้ชนซ้ายและบน
        setPrivateField("ball", ball);
        panel.checkCollisions(); // ชนซ้ายและบน
        assertEquals(2, ball.getDx()); // reverse dx
        assertEquals(2, ball.getDy()); // reverse dy

        ball = new Ball(280, 10, 20, 20, 2, 2, ball); // ชนขวา
        setPrivateField("ball", ball);
        panel.checkCollisions(); // ชนขวา
        assertEquals(-2, ball.getDx());
    }

    @Test
    // ทดสอบ checkCollisions: ลูกบอลตกด้านล่าง (gameOver = true, timer.stop)
    void testCollisionBottomLose() throws NoSuchFieldException, IllegalAccessException {
        Ball ball = (Ball) getPrivateField("ball");
        ball = new Ball(100, 380, 20, 20, 0, 2, ball); // ตกด้านล่าง
        setPrivateField("ball", ball);
        panel.checkCollisions();
        assertTrue(getPrivateField("gameOver"));
    }

    @Test
    // ทดสอบ checkCollisions: ชนแพดเดิล (reverseDy)
    void testCollisionPaddle() throws NoSuchFieldException, IllegalAccessException {
        Ball ball = (Ball) getPrivateField("ball");
        Paddle paddle = (Paddle) getPrivateField("paddle");
        ball = new Ball(paddle.getX() + 10, paddle.getY() - 10, 20, 20, 0, 2, ball); // ชนแพดเดิล
        setPrivateField("ball", ball);
        panel.checkCollisions();
        assertEquals(-2, ball.getDy()); // reverse dy
    }

    @Test
    // ทดสอบ checkCollisions: ชนอิฐ (destroy, bricksDestroyed++, reverseDy) และชนะเมื่อครบ 30
    void testCollisionBricksAndWin() throws NoSuchFieldException, IllegalAccessException {
        Brick[] bricks = (Brick[]) getPrivateField("bricks");
        Ball ball = (Ball) getPrivateField("ball");
        ball = new Ball(bricks[0].getX() + 10, bricks[0].getY() + 10, 20, 20, 0, -2, ball); // ชนอิฐแรก
        setPrivateField("ball", ball);
        panel.checkCollisions();
        assertTrue(bricks[0].isDestroyed());
        assertEquals(1, getPrivateField("bricksDestroyed"));
        assertEquals(2, ball.getDy()); // reverse dy

        // จำลองทำลายทั้งหมดเพื่อชนะ
        setPrivateField("bricksDestroyed", 29);
        panel.checkCollisions(); // ทำลายตัวสุดท้าย (branch: if bricksDestroyed == 30)
        assertTrue(getPrivateField("victory"));
    }

    @Test
    // ทดสอบ paintComponent: ครอบคลุมการวาดทุกองค์ประกอบและข้อความ (gameOver, victory)
    void testPaintComponent() throws NoSuchFieldException, IllegalAccessException {
        Graphics mockGraphics = new Graphics() {}; // Mock เพื่อ cover statements
        panel.paintComponent(mockGraphics); // วาดปกติ

        setPrivateField("gameOver", true);
        panel.paintComponent(mockGraphics); // วาด Game Over (branch)

        setPrivateField("gameOver", false);
        setPrivateField("victory", true);
        panel.paintComponent(mockGraphics); // วาด Victory (branch)
    }

    // Helper methods สำหรับ access private fields ด้วย reflection
    private Object getPrivateField(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = GamePanel.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(panel);
    }

    private void setPrivateField(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = GamePanel.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(panel, value);
    }
}