package Game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Breakout {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Breakout");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 400); // ขนาดหน้าต่างคงที่ตามข้อกำหนด
            frame.setResizable(false);

            GamePanel panel = new GamePanel();
            frame.add(panel);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // ให้ panel ขอรับ focus เพื่อรับการกดปุ่มลูกศร
            panel.requestFocusInWindow();
        });
    }
}