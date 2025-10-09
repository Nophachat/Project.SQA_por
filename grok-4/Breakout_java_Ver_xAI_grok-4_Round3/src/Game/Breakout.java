package Game;

import javax.swing.JFrame;

public class Breakout {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Breakout");
        GamePanel panel = new GamePanel();
        frame.add(panel);
        frame.setSize(300, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        panel.requestFocusInWindow(); // Ensure panel gets key events
    }
}