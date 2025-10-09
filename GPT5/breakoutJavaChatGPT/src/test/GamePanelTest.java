package test;

import org.junit.jupiter.api.Test;

import Game.Ball;
import Game.Brick;
import Game.GamePanel;
import Game.Paddle;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;

public class GamePanelTest {

    // helper: stop internal timer (prevent background ticks)
    private void stopTimer(GamePanel panel) throws Exception {
        Field f = GamePanel.class.getDeclaredField("timer");
        f.setAccessible(true);
        Timer t = (Timer) f.get(panel);
        if (t != null) t.stop();
    }

    // helper: get private field by name
    @SuppressWarnings("unchecked")
    private <T> T getField(Object obj, String name, Class<T> cls) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return (T) f.get(obj);
    }

    // ทดสอบ initIfNeeded เมื่อ panel มีขนาด (300x400) => สร้าง paddle, ball, bricks (30)
    @Test
    public void testInitCreatesComponents() throws Exception {
        GamePanel panel = new GamePanel();
        // stop timer
        stopTimer(panel);

        // set size to expected window size so initIfNeeded makes proper layout
        SwingUtilities.invokeAndWait(() -> panel.setSize(300, 400));

        // call paint to trigger initIfNeeded on EDT
        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        SwingUtilities.invokeAndWait(() -> panel.paint(g));

        // access private fields
        Object paddle = getField(panel, "paddle", Object.class);
        Object ball = getField(panel, "ball", Object.class);
        List<?> bricks = getField(panel, "bricks", List.class);

        assertNotNull(paddle, "paddle ต้องถูกสร้าง");
        assertNotNull(ball, "ball ต้องถูกสร้าง");
        assertNotNull(bricks, "bricks ต้องไม่เป็น null");
        assertEquals(30, bricks.size(), "ต้องมี bricks 30 ก้อน (5x6)");
    }

    // ทดสอบการชนผนังซ้าย/ขวา/บน (flip dx/dy)
    @Test
    public void testWallCollisions() throws Exception {
        GamePanel panel = new GamePanel();
        stopTimer(panel);

        SwingUtilities.invokeAndWait(() -> panel.setSize(300, 400));
        // init
        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        SwingUtilities.invokeAndWait(() -> panel.paint(g));

        // get ball
        Ball ball = getField(panel, "ball", Ball.class);

        // LEFT wall: set position so after move it's <=0
        ball.setX(1);
        ball.setDx(-5); // big negative so x+dx <=0
        ball.setY(100);
        ball.setDy(0);

        SwingUtilities.invokeAndWait(() -> panel.actionPerformed(null));
        assertEquals(0, ball.getX(), "เมื่อชนผนังซ้าย x ต้องถูกตั้งเป็น 0");
        assertTrue(ball.getDx() > 0, "dx ต้องถูกกลับเครื่องหมายเมื่อชนผนังซ้าย");

        // RIGHT wall: set position so x+diameter >= width
        ball.setX(300 - ball.getDiameter() - 1);
        ball.setDx(5);
        SwingUtilities.invokeAndWait(() -> panel.actionPerformed(null));
        assertEquals(300 - ball.getDiameter(), ball.getX(), "เมื่อชนผนังขวา x ต้องถูกตั้งเป็น panelWidth - diameter");
        assertTrue(ball.getDx() < 0, "dx ต้องกลับเครื่องหมายเมื่อชนผนังขวา");

        // TOP wall: set y small and dy negative to cause top collision
        ball.setY(1);
        ball.setDy(-5);
        SwingUtilities.invokeAndWait(() -> panel.actionPerformed(null));
        assertEquals(0, ball.getY(), "เมื่อชนผนังบน y ต้องถูกตั้งเป็น 0");
        assertTrue(ball.getDy() > 0, "dy ต้องกลับเครื่องหมายเมื่อชนผนังบน");
    }

    // ทดสอบการชน Paddle และการปรับทิศทางขึ้น (dy เป็นลบ) และปรับ dx ตามตำแหน่งการชน
    @Test
    public void testPaddleCollisionAdjustsBallDirection() throws Exception {
        GamePanel panel = new GamePanel();
        stopTimer(panel);

        SwingUtilities.invokeAndWait(() -> panel.setSize(300, 400));
        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        SwingUtilities.invokeAndWait(() -> panel.paint(g));

        Ball ball = getField(panel, "ball", Ball.class);
        Paddle paddle = getField(panel, "paddle", Paddle.class);

        // place paddle center at middle; set ball above paddle and moving downwards so it will intersect
        paddle = new Paddle((300 - 60) / 2, 360, 60, 8, 300);
        // replace panel.paddle via reflection to a known paddle position (to control center)
        Field pf = GamePanel.class.getDeclaredField("paddle");
        pf.setAccessible(true);
        pf.set(panel, paddle);

        // set ball slightly above and with dy positive so after move it hits paddle
        ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getDiameter() / 2); // center
        ball.setY(paddle.getY() - ball.getDiameter() - 2);
        ball.setDx(0);
        ball.setDy(3);

        SwingUtilities.invokeAndWait(() -> panel.actionPerformed(null));

        assertTrue(ball.getDy() < 0, "หลังชน paddle dy ต้องเป็นค่าลบ (เด้งขึ้น)");
        assertNotEquals(0, ball.getDx(), "dx ต้องถูกปรับให้ไม่เป็น 0 หลังชนกลาง (โค้ดจะปรับเป็น 1 หรือ -1 เพื่อหลีกเลี่ยง 0)");
    }

    // ทดสอบการชน Brick: brick ต้องถูก setDestroyed และ dy ต้องถูกกลับเครื่องหมาย
    @Test
    public void testBrickCollisionDestroysBrickAndBounces() throws Exception {
        GamePanel panel = new GamePanel();
        stopTimer(panel);

        SwingUtilities.invokeAndWait(() -> panel.setSize(300, 400));
        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        SwingUtilities.invokeAndWait(() -> panel.paint(g));

        @SuppressWarnings("unchecked")
        List<Brick> bricks = getField(panel, "bricks", List.class);
        Ball ball = getField(panel, "ball", Ball.class);

        assertTrue(bricks.size() >= 1, "ควรมี brick อย่างน้อย 1 ก้อน");

        // เลือก brick แรกที่ยังไม่ถูกทำลาย
        Brick target = null;
        for (Brick br : bricks) {
            if (!br.isDestroyed()) {
                target = br;
                break;
            }
        }
        assertNotNull(target, "ต้องมี brick ที่ยังไม่ถูกทำลาย");

        // วางลูกบอลให้ชน brick โดยตั้ง dy = 2 (เมื่อชนจะถูก invert)
        ball.setX(target.getRect().x);
        ball.setY(target.getRect().y);
        ball.setDx(0);
        ball.setDy(2);

        SwingUtilities.invokeAndWait(() -> panel.actionPerformed(null));

        assertTrue(target.isDestroyed(), "เมื่อชน brick แล้ว brick ต้องถูกทำลาย (destroyed == true)");
        assertEquals(-2, ball.getDy(), "dy ต้องถูกกลับเครื่องหมายหลังชน brick");
    }

    // ทดสอบ Victory: ทำลายทุก brick ให้เหลือ 1 ก้อน แล้วชนก้อนสุดท้าย => victory true และ ball หยุด
    @Test
    public void testVictoryCondition() throws Exception {
        GamePanel panel = new GamePanel();
        stopTimer(panel);

        SwingUtilities.invokeAndWait(() -> panel.setSize(300, 400));
        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        SwingUtilities.invokeAndWait(() -> panel.paint(g));

        @SuppressWarnings("unchecked")
        List<Brick> bricks = getField(panel, "bricks", List.class);
        Ball ball = getField(panel, "ball", Ball.class);

        // Mark all bricks destroyed except last one
        for (int i = 0; i < bricks.size() - 1; i++) {
            bricks.get(i).setDestroyed(true);
        }
        Brick last = bricks.get(bricks.size() - 1);
        last.setDestroyed(false);

        // position ball overlapping last brick and dy positive
        ball.setX(last.getRect().x);
        ball.setY(last.getRect().y);
        ball.setDx(1);
        ball.setDy(2);

        SwingUtilities.invokeAndWait(() -> panel.actionPerformed(null));

        // victory flag true and ball stopped (dx==0, dy==0)
        Boolean victory = getField(panel, "victory", Boolean.class);
        assertTrue(victory.booleanValue(), "หลังทำลาย brick ครบ 30 ก้อน ต้องเป็น victory");
        assertEquals(0, ball.getDx(), "เมื่อ victory แล้ว ball.dx ต้องเป็น 0");
        assertEquals(0, ball.getDy(), "เมื่อ victory แล้ว ball.dy ต้องเป็น 0");
    }

    // ทดสอบ Game Over: วาง ball ให้ตกเลยขอบล่าง => gameOver true และ ball หยุด
    @Test
    public void testGameOverCondition() throws Exception {
        GamePanel panel = new GamePanel();
        stopTimer(panel);

        SwingUtilities.invokeAndWait(() -> panel.setSize(300, 400));
        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        SwingUtilities.invokeAndWait(() -> panel.paint(g));

        Ball ball = getField(panel, "ball", Ball.class);

        // วาง ball ให้ y > height เพื่อเรียก game over branch
        ball.setY(1000);
        ball.setDx(2);
        ball.setDy(3);

        SwingUtilities.invokeAndWait(() -> panel.actionPerformed(null));

        Boolean gameOver = getField(panel, "gameOver", Boolean.class);
        assertTrue(gameOver.booleanValue(), "เมื่อ ball หลุดขอบล่าง ต้องเป็น gameOver");
        assertEquals(0, ball.getDx(), "เมื่อ gameOver แล้ว ball.dx ต้องเป็น 0");
        assertEquals(0, ball.getDy(), "เมื่อ gameOver แล้ว ball.dy ต้องเป็น 0");
    }

    // ทดสอบ KeyListener: ส่ง KeyEvent เพื่อ set leftPressed / rightPressed
    @Test
    public void testKeyPressReleaseSetsFlags() throws Exception {
        GamePanel panel = new GamePanel();
        stopTimer(panel);

        SwingUtilities.invokeAndWait(() -> panel.setSize(300, 400));
        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        SwingUtilities.invokeAndWait(() -> panel.paint(g));

        // ส่ง KeyEvent (pressed) และ (released) โดยใช้ dispatchEvent บน EDT
        SwingUtilities.invokeAndWait(() -> {
            java.awt.event.KeyEvent pressLeft = new java.awt.event.KeyEvent(panel, java.awt.event.KeyEvent.KEY_PRESSED,
                    System.currentTimeMillis(), 0, java.awt.event.KeyEvent.VK_LEFT, (char) 0);
            panel.dispatchEvent(pressLeft);

            java.awt.event.KeyEvent releaseLeft = new java.awt.event.KeyEvent(panel, java.awt.event.KeyEvent.KEY_RELEASED,
                    System.currentTimeMillis(), 0, java.awt.event.KeyEvent.VK_LEFT, (char) 0);
            panel.dispatchEvent(releaseLeft);

            java.awt.event.KeyEvent pressRight = new java.awt.event.KeyEvent(panel, java.awt.event.KeyEvent.KEY_PRESSED,
                    System.currentTimeMillis(), 0, java.awt.event.KeyEvent.VK_RIGHT, (char) 0);
            panel.dispatchEvent(pressRight);

            java.awt.event.KeyEvent releaseRight = new java.awt.event.KeyEvent(panel, java.awt.event.KeyEvent.KEY_RELEASED,
                    System.currentTimeMillis(), 0, java.awt.event.KeyEvent.VK_RIGHT, (char) 0);
            panel.dispatchEvent(releaseRight);
        });

        // ตรวจสถานะ leftPressed/rightPressed ผ่าน reflection
        Boolean leftPressed = getField(panel, "leftPressed", Boolean.class);
        Boolean rightPressed = getField(panel, "rightPressed", Boolean.class);

        // เพราะเรา pressed แล้ว released ทั้งคู่ ค่าสุดท้ายต้องเป็น false
        assertFalse(leftPressed.booleanValue(), "หลัง pressed+released ควรเป็น false");
        assertFalse(rightPressed.booleanValue(), "หลัง pressed+released ควรเป็น false");
    }

    // ทดสอบ paint overlay เมื่อ victory/gameOver (ตรวจว่าไม่เกิด exception)
    @Test
    public void testPaintOverlayWhenGameEnds() throws Exception {
        GamePanel panel = new GamePanel();
        stopTimer(panel);

        SwingUtilities.invokeAndWait(() -> panel.setSize(300, 400));
        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        SwingUtilities.invokeAndWait(() -> panel.paint(g));

        // set both flags separately and paint to cover overlay branch
        Field gv = GamePanel.class.getDeclaredField("victory");
        Field gf = GamePanel.class.getDeclaredField("gameOver");
        gv.setAccessible(true);
        gf.setAccessible(true);

        // victory overlay
        gv.setBoolean(panel, true);
        gf.setBoolean(panel, false);
        SwingUtilities.invokeAndWait(() -> panel.paint(g));

        // game over overlay
        gv.setBoolean(panel, false);
        gf.setBoolean(panel, true);
        SwingUtilities.invokeAndWait(() -> panel.paint(g));

        // also test early return in actionPerformed when flags true (timer.stop + repaint)
        SwingUtilities.invokeAndWait(() -> panel.actionPerformed(null));
    }

    // ทดสอบ initIfNeeded เมื่อขนาดเป็น 0 (สาขาใน initIfNeeded ที่ถูกเรียกแม้ getWidth()==0)
    @Test
    public void testInitIfNeededWithZeroSize() throws Exception {
        GamePanel panel = new GamePanel();
        // stop timer to avoid ticks
        stopTimer(panel);

        // Do NOT set size -> default width/height == 0
        // Call actionPerformed which internally will call initIfNeeded
        SwingUtilities.invokeAndWait(() -> panel.actionPerformed(null));

        // ตรวจว่า panel ไม่ throw exception และ field initialized ถูกตั้งหรือไม่ (อาจเป็น true)
        Boolean initialized = getField(panel, "initialized", Boolean.class);
        assertNotNull(initialized, "field initialized ต้องมีอยู่");
    }
}
