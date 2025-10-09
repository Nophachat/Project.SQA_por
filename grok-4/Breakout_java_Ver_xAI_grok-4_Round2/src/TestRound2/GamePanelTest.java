package TestRound2;

import Game.Breakout;
import Game.Paddle;
import Game.Brick;
import Game.Ball;
import Game.GamePanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        assertFalse(getPrivateBoolean("gameOver"));
        assertFalse(getPrivateBoolean("victory"));
        assertEquals(0, getPrivateInt("bricksDestroyed"));
    }

    @Test
    // ทดสอบ actionPerformed: กรณีเกมกำลังเล่น (เรียก move และ checkCollisions) และกรณีจบเกม (ไม่เรียก)
    void testActionPerformed() throws NoSuchFieldException, IllegalAccessException {
        ActionEvent e = new ActionEvent(this, 0, null);
        Ball ball = (Ball) getPrivateObject("ball");
        int initialY = ball.getY();
        panel.actionPerformed(e); // เกมกำลังเล่น: move ถูกเรียก
        assertNotEquals(initialY, ((Ball) getPrivateObject("ball")).getY());

        setPrivateBoolean("gameOver", true);
        initialY = ((Ball) getPrivateObject("ball")).getY();
        panel.actionPerformed(e); // เกมจบ: ไม่ move (branch: if !gameOver && !victory)
        assertEquals(initialY, ((Ball) getPrivateObject("ball")).getY());
    }

    @Test
    // ทดสอบ checkCollisions: ชนกำแพงซ้าย/ขวา/บน (reverse dx/dy)
    void testCollisionWalls() throws NoSuchFieldException, IllegalAccessException {
        Ball ball = new Ball(0, 0, 20, 20, -2, -2);
        setPrivateObject("ball", ball);
        panel.checkCollisions(); // ชนซ้ายและบน
        assertEquals(2, ball.getDx()); // reverse dx
        assertEquals(2, ball.getDy()); // reverse dy

        ball = new Ball(280, 10, 20, 20, 2, 2);
        setPrivateObject("ball", ball);
        panel.checkCollisions(); // ชนขวา
        assertEquals(-2, ball.getDx());
    }

    @Test
    // ทดสอบ checkCollisions: ลูกบอลตกด้านล่าง (gameOver = true, timer.stop)
    void testCollisionBottomLose() throws NoSuchFieldException, IllegalAccessException {
        Ball ball = new Ball(100, 380, 20, 20, 0, 2);
        setPrivateObject("ball", ball);
        panel.checkCollisions();
        assertTrue(getPrivateBoolean("gameOver"));
    }

    @Test
    // ทดสอบ checkCollisions: ชนแพดเดิล (reverseDy)
    void testCollisionPaddle() throws NoSuchFieldException, IllegalAccessException {
        Paddle paddle = (Paddle) getPrivateObject("paddle");
        Ball ball = new Ball(paddle.getX() + 10, paddle.getY() - 10, 20, 20, 0, 2);
        setPrivateObject("ball", ball);
        panel.checkCollisions();
        assertEquals(-2, ball.getDy()); // reverse dy
    }

    @Test
    // ทดสอบ checkCollisions: ชนอิฐ (destroy, bricksDestroyed++, reverseDy) และชนะเมื่อครบ 30
    void testCollisionBricksAndWin() throws NoSuchFieldException, IllegalAccessException {
        Brick[] bricks = (Brick[]) getPrivateObject("bricks");
        Ball ball = new Ball(bricks[0].getX() + 10, bricks[0].getY() + 10, 20, 20, 0, -2);
        setPrivateObject("ball", ball);
        panel.checkCollisions();
        assertTrue(bricks[0].isDestroyed());
        assertEquals(1, getPrivateInt("bricksDestroyed"));
        assertEquals(2, ball.getDy()); // reverse dy

        // จำลองทำลายเกือบหมด แล้วชนตัวสุดท้ายเพื่อชนะ
        for (int i = 1; i < 30; i++) {
            bricks[i].destroy();
        }
        setPrivateInt("bricksDestroyed", 29);
        // ตั้ง ball ให้ชน brick[0] (ซึ่งยังไม่ destroy ใน simulation นี้? ไม่ ตัว [0] destroy แล้ว แต่เราสามารถ target ตัวอื่น; ที่จริง [0] destroy แล้ว ดังนั้น target [1] แต่เนื่องจาก loop destroy 1-29, [0] ยังอยู่
        // Fix: destroy 0-28, set to 29, then hit [29]
        for (int i = 0; i < 29; i++) {
            bricks[i].destroy();
        }
        setPrivateInt("bricksDestroyed", 29);
        ball = new Ball(bricks[29].getX() + 10, bricks[29].getY() + 10, 20, 20, 0, -2);
        setPrivateObject("ball", ball);
        panel.checkCollisions(); // ทำลายตัวสุดท้าย (branch: if bricksDestroyed == 30)
        assertTrue(getPrivateBoolean("victory"));
    }

    @Test
    // ทดสอบ paintComponent: ครอบคลุมการวาดทุกองค์ประกอบและข้อความ (gameOver, victory)
    void testPaintComponent() throws NoSuchFieldException, IllegalAccessException {
        Graphics mockGraphics = new Graphics() {
            @Override public void fillRect(int x, int y, int w, int h) {} // Mock methods to cover calls
            @Override public void fillOval(int x, int y, int w, int h) {}
            @Override public void drawString(String str, int x, int y) {}
            @Override public void setColor(java.awt.Color c) {}
        };
        panel.paintComponent(mockGraphics); // วาดปกติ (ครอบคลุม loop วาด bricks)

        setPrivateBoolean("gameOver", true);
        panel.paintComponent(mockGraphics); // วาด Game Over (branch)

        setPrivateBoolean("gameOver", false);
        setPrivateBoolean("victory", true);
        panel.paintComponent(mockGraphics); // วาด Victory (branch)
    }

    // Helpers for reflection (typed for clarity)
    private Object getPrivateObject(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = GamePanel.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(panel);
    }

    private boolean getPrivateBoolean(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return (boolean) getPrivateObject(fieldName);
    }

    private int getPrivateInt(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return (int) getPrivateObject(fieldName);
    }

    private void setPrivateObject(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = GamePanel.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(panel, value);
    }

    private void setPrivateBoolean(String fieldName, boolean value) throws NoSuchFieldException, IllegalAccessException {
        setPrivateObject(fieldName, value);
    }

    private void setPrivateInt(String fieldName, int value) throws NoSuchFieldException, IllegalAccessException {
        setPrivateObject(fieldName, value);
    }
}