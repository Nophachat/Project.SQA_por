package TestRound3;

import Game.Ball;
import Game.Brick;
import Game.GamePanel;
import Game.Paddle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.awt.image.BufferedImage; // Added for real Graphics instance

public class GamePanelTest {
    private GamePanel panel;

    @BeforeEach
    void setUp() {
        panel = new GamePanel();
    }

    @Test
    // ทดสอบ constructor รวมถึง initBricks และการตั้งค่าเริ่มต้น (ครอบคลุม loop ใน initBricks)
    void testConstructorAndInitBricks() throws Exception {
        Brick[] bricks = (Brick[]) getPrivateField("bricks");
        assertEquals(30, bricks.length);
        assertFalse((boolean) getPrivateField("gameOver"));
        assertFalse((boolean) getPrivateField("victory"));
        assertEquals(0, (int) getPrivateField("bricksDestroyed"));
    }

    @Test
    // ทดสอบ actionPerformed: กรณีเล่น (เรียก move, checkCollisions, repaint) และจบเกม (ไม่เรียก, แต่ repaint ยังทำงาน)
    void testActionPerformed() throws Exception {
        ActionEvent e = new ActionEvent(panel, 0, null);
        Ball ball = (Ball) getPrivateField("ball");
        int initialY = ball.getY();
        panel.actionPerformed(e); // เล่น: move ทำงาน
        assertNotEquals(initialY, ((Ball) getPrivateField("ball")).getY());

        setPrivateField("gameOver", true);
        initialY = ((Ball) getPrivateField("ball")).getY();
        panel.actionPerformed(e); // จบ: ไม่ move (branch)
        assertEquals(initialY, ((Ball) getPrivateField("ball")).getY());
    }

    @Test
    // ทดสอบ checkCollisions: ชนกำแพง (branches ใน if walls)
    void testCollisionWalls() throws Exception {
        Ball ball = new Ball(0, 0, 20, 20, -2, -2);
        setPrivateField("ball", ball);
        panel.checkCollisions();
        assertEquals(2, ball.getDx());
        assertEquals(2, ball.getDy());

        ball = new Ball(280, 10, 20, 20, 2, 2);
        setPrivateField("ball", ball);
        panel.checkCollisions();
        assertEquals(-2, ball.getDx());
    }

    @Test
    // ทดสอบ checkCollisions: ตกด้านล่าง (gameOver=true, return)
    void testCollisionBottomLose() throws Exception {
        Ball ball = new Ball(100, 380, 20, 20, 0, 2);
        setPrivateField("ball", ball);
        panel.checkCollisions();
        assertTrue((boolean) getPrivateField("gameOver"));
    }

    @Test
    // ทดสอบ checkCollisions: ชนแพดเดิล (reverseDy)
    void testCollisionPaddle() throws Exception {
        Paddle paddle = (Paddle) getPrivateField("paddle");
        Ball ball = new Ball(paddle.getX() + 10, paddle.getY() - 10, 20, 20, 0, 2);
        setPrivateField("ball", ball);
        panel.checkCollisions();
        assertEquals(-2, ball.getDy());
    }

    @Test
    // ทดสอบ checkCollisions: ชนอิฐ (destroy, ++, reverseDy, break) และชนะ (if ==30); รวมกรณีไม่ชน (loop เต็ม)
    void testCollisionBricksAndWin() throws Exception {
        Brick[] bricks = (Brick[]) getPrivateField("bricks");
        Ball ball = new Ball(bricks[0].getX() + 10, bricks[0].getY() + 10, 20, 20, 0, -2);
        setPrivateField("ball", ball);
        panel.checkCollisions(); // ชน 1 ตัว (break ทำงาน)
        assertTrue(bricks[0].isDestroyed());
        assertEquals(1, (int) getPrivateField("bricksDestroyed"));
        assertEquals(2, ball.getDy());

        // จำลองไม่ชน (loop เต็ม, no break)
        ball = new Ball(0, 0, 20, 20, 0, 0); // ไม่ intersects
        setPrivateField("ball", ball);
        panel.checkCollisions(); // ไม่ชนอะไร

        // ชนะ: ทำลาย 29, แล้วชนตัวสุดท้าย
        for (int i = 0; i < 29; i++) bricks[i].destroy();
        setPrivateField("bricksDestroyed", 29);
        ball = new Ball(bricks[29].getX() + 10, bricks[29].getY() + 10, 20, 20, 0, -2);
        setPrivateField("ball", ball);
        panel.checkCollisions();
        assertTrue((boolean) getPrivateField("victory"));
    }

    @Test
    // ทดสอบ paintComponent: วาดทุกอย่าง (loop bricks with if !destroyed) และ branches ข้อความ
    void testPaintComponent() throws Exception {
        // Use real Graphics from BufferedImage to avoid abstract method errors
        BufferedImage image = new BufferedImage(300, 400, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        panel.paintComponent(g); // วาดปกติ

        setPrivateField("gameOver", true);
        panel.paintComponent(g); // Game Over branch

        setPrivateField("gameOver", false);
        setPrivateField("victory", true);
        panel.paintComponent(g); // Victory branch

        g.dispose(); // Clean up
    }

    @Test
    // ทดสอบ KeyListener ใน constructor (anonymous KeyAdapter): branches ใน keyPressed
    void testKeyListener() throws Exception {
        // Get anonymous KeyListener via reflection
        Field listenersField = java.awt.Component.class.getDeclaredField("listenerList");
        listenersField.setAccessible(true);
        Object listenerList = listenersField.get(panel);
        Method getListenersMethod = listenerList.getClass().getMethod("getListeners", Class.class);
        KeyListener[] listeners = (KeyListener[]) getListenersMethod.invoke(listenerList, KeyListener.class);
        KeyListener keyListener = listeners[0]; // The anonymous one

        Paddle paddle = (Paddle) getPrivateField("paddle");
        int initialX = paddle.getX();

        // กด key เมื่อเล่น (branch true)
        KeyEvent leftEvent = new KeyEvent(panel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, ' ');
        keyListener.keyPressed(leftEvent);
        assertTrue(paddle.getX() < initialX);

        // กด key เมื่อจบ (branch false)
        setPrivateField("gameOver", true);
        initialX = paddle.getX();
        keyListener.keyPressed(leftEvent);
        assertEquals(initialX, paddle.getX());
    }

    private Object getPrivateField(String name) throws Exception {
        Field field = GamePanel.class.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(panel);
    }

    private void setPrivateField(String name, Object value) throws Exception {
        Field field = GamePanel.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(panel, value);
    }
}