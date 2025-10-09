package test;
import org.junit.jupiter.api.Test;

import Game.Paddle;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;

public class PaddleTest {

    // ทดสอบการเคลื่อนที่ไปทางซ้ายและขวา และการไม่ทะลุขอบ (checkBounds)
    @Test
    public void testMoveLeftRightAndBounds() {
        // สร้าง Paddle ที่อยู่กลาง panel กว้าง 300
        Paddle p = new Paddle(120, 350, 60, 8, 300);

        // moveLeft ปกติ
        p.moveLeft();
        assertTrue(p.getX() < 120, "หลัง moveLeft x ต้องน้อยกว่าเดิม");

        // moveLeft ไปจนเกินขอบซ้าย ต้องถูก clamp ที่ 0
        for (int i = 0; i < 100; i++) p.moveLeft();
        assertEquals(0, p.getX(), "Paddle ต้องไม่เคลื่อนออกนอกซ้าย (x==0)");

        // moveRight ปกติ
        p.moveRight();
        assertTrue(p.getX() > 0, "หลัง moveRight x ต้องเพิ่มขึ้น");

        // moveRight ไปจนเกินขอบขวา ต้องถูก clamp ที่ panelWidth - width
        for (int i = 0; i < 200; i++) p.moveRight();
        assertEquals(300 - p.getWidth(), p.getX(), "Paddle ต้องไม่เคลื่อนออกนอกขวา");

        // checkBounds ให้แน่ใจว่าฟังก์ชัน clamp สามารถเรียกแยกได้
        p = new Paddle(-50, 350, 60, 8, 300);
        p.checkBounds();
        assertEquals(0, p.getX(), "checkBounds ต้องปรับตำแหน่งที่น้อยกว่า 0 ให้เป็น 0");

        p = new Paddle(500, 350, 60, 8, 300);
        p.checkBounds();
        assertEquals(240, p.getX(), "checkBounds ต้องปรับตำแหน่งที่เกินขวาให้ไม่เกิน (panelWidth-width)");

    }

    // ทดสอบ draw() และ getRect()
    @Test
    public void testDrawAndGetRect() {
        Paddle p = new Paddle(100, 300, 60, 8, 300);
        BufferedImage img = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        // ฟังก์ชัน draw ไม่ควรโยน exception
        p.draw(g);

        Rectangle r = p.getRect();
        assertEquals(100, r.x);
        assertEquals(300, r.y);
        assertEquals(60, r.width);
        assertEquals(8, r.height);
    }
}
