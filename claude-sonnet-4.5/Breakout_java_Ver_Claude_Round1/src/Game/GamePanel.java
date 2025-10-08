package Game;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Ball ball;
    private Paddle paddle;
    private Brick[][] bricks;
    private boolean gameOver = false;
    private boolean victory = false;
    private int bricksRemaining = 30;
    
    private static final int BRICK_ROWS = 5;
    private static final int BRICK_COLS = 6;
    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 400;
    
    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        // สร้างองค์ประกอบของเกม
        ball = new Ball(140, 200);
        paddle = new Paddle(115, 350);
        
        // สร้างอิฐ - ปรับให้ไม่เกินขอบหน้าต่าง
        bricks = new Brick[BRICK_ROWS][BRICK_COLS];
        
        int margin = 10; // ระยะห่างจากขอบซ้าย-ขวา
        int brickGap = 3; // ระยะห่างระหว่างอิฐ
        int startY = 50; // ตำแหน่งเริ่มต้นแนวตั้ง
        
        // คำนวณความกว้างของอิฐให้พอดีกับหน้าจอ
        int totalWidth = PANEL_WIDTH - (2 * margin);
        int totalGaps = (BRICK_COLS - 1) * brickGap;
        int brickWidth = (totalWidth - totalGaps) / BRICK_COLS;
        int brickHeight = 15;
        
        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < BRICK_COLS; col++) {
                int x = margin + col * (brickWidth + brickGap);
                int y = startY + row * (brickHeight + brickGap);
                bricks[row][col] = new Brick(x, y, brickWidth, brickHeight);
            }
        }
        
        // เริ่มเกมทันที
        timer = new Timer(10, this);
        timer.start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // วาดอิฐ
        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < BRICK_COLS; col++) {
                if (!bricks[row][col].isDestroyed()) {
                    bricks[row][col].draw(g);
                }
            }
        }
        
        // วาดแพดเดิลและลูกบอล
        paddle.draw(g);
        ball.draw(g);
        
        // แสดงข้อความเมื่อเกมจบ
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", 70, 200);
        }
        
        if (victory) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Victory", 85, 200);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !victory) {
            // เคลื่อนที่ลูกบอล
            ball.move();
            
            // ตรวจสอบการชนกับกำแพง
            if (ball.getX() <= 0 || ball.getX() >= getWidth() - ball.getSize()) {
                ball.reverseX();
            }
            
            if (ball.getY() <= 0) {
                ball.reverseY();
            }
            
            // ตรวจสอบการตกด้านล่าง (แพ้)
            if (ball.getY() >= getHeight()) {
                gameOver = true;
                timer.stop();
            }
            
            // ตรวจสอบการชนกับแพดเดิล
            if (ball.intersects(paddle.getX(), paddle.getY(), 
                               paddle.getWidth(), paddle.getHeight())) {
                ball.reverseY();
            }
            
            // ตรวจสอบการชนกับอิฐ
            for (int row = 0; row < BRICK_ROWS; row++) {
                for (int col = 0; col < BRICK_COLS; col++) {
                    Brick brick = bricks[row][col];
                    if (!brick.isDestroyed() && 
                        ball.intersects(brick.getX(), brick.getY(), 
                                       brick.getWidth(), brick.getHeight())) {
                        brick.destroy();
                        ball.reverseY();
                        bricksRemaining--;
                        
                        // ตรวจสอบเงื่อนไขชนะ
                        if (bricksRemaining == 0) {
                            victory = true;
                            timer.stop();
                        }
                    }
                }
            }
        }
        
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && !victory) {
            int key = e.getKeyCode();
            
            if (key == KeyEvent.VK_LEFT) {
                paddle.moveLeft(getWidth());
            }
            
            if (key == KeyEvent.VK_RIGHT) {
                paddle.moveRight(getWidth());
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
}