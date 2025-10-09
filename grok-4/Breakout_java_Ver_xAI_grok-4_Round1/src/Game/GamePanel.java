package Game;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements ActionListener {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 400;
    private static final int DELAY = 10; // Milliseconds between updates

    private Timer timer;
    private Paddle paddle;
    private Ball ball;
    private Brick[] bricks;
    private boolean gameOver = false;
    private boolean victory = false;
    private int bricksDestroyed = 0;
    private static final int NUM_BRICKS = 30;
    private static final int ROWS = 5;
    private static final int COLS = 6;
    private static final int BRICK_WIDTH = WIDTH / COLS;
    private static final int BRICK_HEIGHT = 20;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver && !victory) {
                    paddle.move(e.getKeyCode());
                }
            }
        });

        // Initialize game objects
        paddle = new Paddle(WIDTH / 2 - 30, HEIGHT - 50, 60, 10);
        ball = new Ball(WIDTH / 2 - 10, HEIGHT / 2, 20, 20, 2, -2); // Initial velocity
        bricks = new Brick[NUM_BRICKS];
        initBricks();

        // Start game automatically
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void initBricks() {
        int index = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * BRICK_WIDTH;
                int y = row * BRICK_HEIGHT + 20; // Offset from top
                bricks[index++] = new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !victory) {
            ball.move();
            checkCollisions();
        }
        repaint();
    }

    private void checkCollisions() {
        // Ball with walls
        if (ball.getX() <= 0 || ball.getX() >= WIDTH - ball.getWidth()) {
            ball.reverseDx();
        }
        if (ball.getY() <= 0) {
            ball.reverseDy();
        }
        if (ball.getY() >= HEIGHT - ball.getHeight()) {
            gameOver = true;
            timer.stop();
            return;
        }

        // Ball with paddle
        if (ball.intersects(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight())) {
            ball.reverseDy(); // Simple realistic bounce (reverse vertical)
        }

        // Ball with bricks
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.intersects(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight())) {
                brick.destroy();
                bricksDestroyed++;
                ball.reverseDy(); // Simple realistic bounce (reverse vertical)
                if (bricksDestroyed == NUM_BRICKS) {
                    victory = true;
                    timer.stop();
                }
                break; // Only destroy one brick per frame
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw paddle
        g.setColor(Color.WHITE);
        g.fillRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

        // Draw ball
        g.setColor(Color.RED);
        g.fillOval(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());

        // Draw bricks
        g.setColor(Color.GREEN);
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                g.fillRect(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
            }
        }

        // Draw game over or victory message
        g.setColor(Color.WHITE);
        if (gameOver) {
            g.drawString("Game Over", WIDTH / 2 - 40, HEIGHT / 2);
        } else if (victory) {
            g.drawString("Victory", WIDTH / 2 - 30, HEIGHT / 2);
        }
    }
}