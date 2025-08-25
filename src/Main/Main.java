package Main;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Main {
    public static JFrame window;
    private static GamePanel gamePanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            gamePanel = new GamePanel();

            window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setTitle("Drinky Way");
            URL iconURL = Main.class.getResource("/res/icon/icon.png");
            if (iconURL != null) window.setIconImage(new ImageIcon(iconURL).getImage());

            window.setUndecorated(true);
            window.setContentPane(gamePanel);
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            gd.setFullScreenWindow(window);
            window.setVisible(true);

            SwingUtilities.invokeLater(() -> {
                try {
                    window.createBufferStrategy(3);
                    gamePanel.attachBufferStrategy(window.getBufferStrategy());
                } catch (IllegalStateException ignored) {}
            });

            gamePanel.setupGame();
            gamePanel.requestFocusInWindow();
            gamePanel.startGameThread();
        });
    }
}