package Main;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Main {
    public static JFrame window;
    private static GamePanel gamePanel;
    public static void main(String[] args) { init(); }

    private static void init(){
        SwingUtilities.invokeLater(() -> {
            gamePanel = new GamePanel();
            window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setTitle("Drinky Way");
            URL iconURL = Main.class.getResource("/res/icon/icon.png");
            if (iconURL != null) window.setIconImage(new ImageIcon(iconURL).getImage());

            gamePanel.config.loadConfig();
            window.setContentPane(gamePanel);

            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            System.setProperty("sun.java2d.ddforcevram", "true");
            System.setProperty("sun.java2d.d3d","true");
            System.setProperty("sun.java2d.translaccel","true");
            window.setUndecorated(true);
            window.setResizable(false);
            window.setVisible(true);
            if(gd.isFullScreenSupported()) {
                try { gd.setFullScreenWindow(window); } catch (Exception ignored) {}
            } else {
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            }

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