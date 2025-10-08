package Game;

import javax.swing.JFrame;

public class BreakoutGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Breakout");
        GamePanel gamePanel = new GamePanel();
        
        frame.add(gamePanel);
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}