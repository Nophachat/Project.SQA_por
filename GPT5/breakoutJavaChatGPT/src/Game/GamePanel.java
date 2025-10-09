package Game;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    private Timer timer;
    private Paddle paddle;
    private Ball ball;
    private ArrayList<Brick> bricks;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean initialized = false;
    private boolean gameOver = false;
    private boolean victory = false;

    private final int ROWS = 5;
    private final int COLS = 6;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int k = e.getKeyCode();
                if (k == KeyEvent.VK_LEFT) leftPressed = true;
                if (k == KeyEvent.VK_RIGHT) rightPressed = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int k = e.getKeyCode();
                if (k == KeyEvent.VK_LEFT) leftPressed = false;
                if (k == KeyEvent.VK_RIGHT) rightPressed = false;
            }
        });

        // เกมเริ่มอัตโนมัติด้วย timer
        timer = new Timer(10, this); // 10 ms per tick
        timer.start();
    }

    private void initIfNeeded() {
        if (initialized) return;
        int w = getWidth();
        int h = getHeight();

        // สร้าง paddle และ ball
        int paddleWidth = 60;
        int paddleHeight = 8;
        paddle = new Paddle((w - paddleWidth) / 2, h - 40, paddleWidth, paddleHeight, w);

        int ballDiameter = 50;
        ball = new Ball(w / 2 - ballDiameter / 2, h / 2, ballDiameter, -1, -2);

        // สร้าง bricks ให้พอดีกับพื้นที่ด้านบน ไม่เกินขอบ
        bricks = new ArrayList<>();
        int marginTop = 30;
        int marginSides = 10;
        int gap = 6;

        int totalGapX = (COLS - 1) * gap;
        int availableWidth = w - 2 * marginSides - totalGapX;
        int brickWidth = availableWidth / COLS;
        int brickHeight = 12;

        // ปรับ marginSides เล็กน้อยเพื่อให้แน่นไม่มีช่องว่างเกินขอบ
        int usedWidth = brickWidth * COLS + totalGapX;
        int extra = w - usedWidth;
        int leftStart = (extra / 2);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = leftStart + c * (brickWidth + gap);
                int y = marginTop + r * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }

        initialized = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        initIfNeeded();

        // วาด paddle, ball, bricks
        paddle.draw(g);
        ball.draw(g);

        for (Brick b : bricks) {
            if (!b.isDestroyed()) b.draw(g);
        }

        // ถ้าเกมจบ ให้แสดงข้อความตรงกลาง
        if (gameOver || victory) {
            g.setColor(new Color(0, 0, 0, 170));
            g.fillRect(0, getHeight() / 2 - 30, getWidth(), 60);

            g.setFont(new Font("SansSerif", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            String msg = victory ? "Victory" : "Game Over";
            int strW = g.getFontMetrics().stringWidth(msg);
            g.drawString(msg, (getWidth() - strW) / 2, getHeight() / 2 + 8);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!initialized) {
            // ถ้ายังไม่ initialize พยายามอีกครั้ง (เช่นก่อน panel มีขนาด)
            initIfNeeded();
        }

        if (gameOver || victory) {
            // หยุดลูกบอล (ไม่อัปเดตตำแหน่ง)
            timer.stop();
            repaint();
            return;
        }

        // อัปเดต paddle ตามปุ่มที่กด
        if (leftPressed) paddle.moveLeft();
        if (rightPressed) paddle.moveRight();
        paddle.checkBounds();

        // เคลื่อนลูกบอล
        ball.move();

        // ตรวจการชนผนังซ้าย/ขวา/บน
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.setDx(-ball.getDx());
        } else if (ball.getX() + ball.getDiameter() >= getWidth()) {
            ball.setX(getWidth() - ball.getDiameter());
            ball.setDx(-ball.getDx());
        }
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setDy(-ball.getDy());
        }

        // ตรวจการชน paddle
        if (ball.getRect().intersects(paddle.getRect())) {
            // เพื่อป้องกันการติด ให้ย้ายลูกบอลขึ้นด้านบนของ paddle
            ball.setY(paddle.getY() - ball.getDiameter());

            // ปรับทิศทางขึ้น และปรับ dx ตามตำแหน่งที่ชนบน paddle (realistic)
            double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
            double ballCenter = ball.getX() + ball.getDiameter() / 2.0;
            double relative = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0); // -1 .. 1
            double maxSpeedX = 4.0;
            ball.setDx((int)(relative * maxSpeedX));
            if (ball.getDx() == 0) {
                // อย่าให้ dx = 0 เลย ปรับเล็กน้อย
                ball.setDx( (relative >= 0) ? 1 : -1 );
            }
            ball.setDy(-Math.abs(ball.getDy())); // รับประกันให้ขึ้น
        }

        // ตรวจการชน bricks
        for (Brick b : bricks) {
            if (!b.isDestroyed() && ball.getRect().intersects(b.getRect())) {
                b.setDestroyed(true);
                // พลิกแกนแนวตั้งเป็นค่าเริ่มต้น (เรียบง่ายและตรงตามข้อกำหนด)
                ball.setDy(-ball.getDy());
                break; // ลบได้ก้อนเดียวต่อ tick เพื่อความเสถียร
            }
        }

        // ตรวจชนะ
        boolean anyLeft = false;
        for (Brick b : bricks) {
            if (!b.isDestroyed()) {
                anyLeft = true;
                break;
            }
        }
        if (!anyLeft) {
            victory = true;
            // หยุดการเคลื่อนที่ของลูกบอล
            ball.setDx(0);
            ball.setDy(0);
        }

        // ตรวจแพ้: ลูกบอลหลุดขอบล่าง
        if (ball.getY() > getHeight()) {
            gameOver = true;
            ball.setDx(0);
            ball.setDy(0);
        }

        repaint();
    }
}