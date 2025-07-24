package Main;

import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;

    Font arial_40,arial_80B;

    public boolean MessageOn = false;
    public String message = "";
    int messageCounter = 0;

    public boolean gameFinished = false;
    double playTime;

    DecimalFormat dFormat=new DecimalFormat("#0.00");

    //CONSTRUCTOR
    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 30);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }

    public void showMessage(String text) {
        message = text;
        MessageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if(gp.gameState==gp.playState){
            //
        }
        if(gp.gameState== gp.pauseState){
            drawPauseScreen();
        }
    }

    public void drawPauseScreen() {
        g2.setFont(arial_80B);
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (gp.screenWidth / 2) - (length / 2);
    }

}
