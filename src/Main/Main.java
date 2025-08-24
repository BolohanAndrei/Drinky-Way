package Main;

import javax.swing.*;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Drinky Way");
        URL iconURL = Main.class.getResource("/res/icon/icon.png");
        assert iconURL != null;
        ImageIcon icon = new ImageIcon(iconURL);
        window.setIconImage(icon.getImage());
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setResizable(true);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();

    }
}