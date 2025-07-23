package Main;

import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UI {

    GamePanel gp;

    Font arial_40,arial_80B;

    BufferedImage goldKeyImage;
    BufferedImage emeraldKeyImage;
    BufferedImage silverKeyImage;

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
        Obj_Gold_Key key = new Obj_Gold_Key();
        Obj_Silver_Key silverKey = new Obj_Silver_Key();
        Obj_Emerald_Key emeraldKey = new Obj_Emerald_Key();
        silverKeyImage = silverKey.image;
        emeraldKeyImage =emeraldKey.image;
        goldKeyImage = key.image;

    }

    public void showMessage(String text) {
        message = text;
        MessageOn = true;
    }

    public void draw(Graphics2D g) {

        if (gameFinished) {
            //FINISHED GAME DISPLAY
            g.setFont(arial_40);
            g.setColor(Color.white);
            String text="You opened a chest, Captain!";
            int textLength= (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
            int x=gp.screenWidth/2-textLength/2;
            int y=gp.screenHeight/2-(gp.tileSize*3);
            g.drawString(text, x, y);

             text="Your time is: "+dFormat.format(playTime)+" seconds!";
             textLength= (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
             x=gp.screenWidth/2-textLength/2;
             y=gp.screenHeight / 2 + (gp.tileSize * 4)+gp.tileSize+20;
            g.drawString(text, x, y);

            g.setFont(arial_80B);
            g.setColor(Color.green);
            g.setFont(arial_80B);
            g.setColor(Color.green);

            String line1 = "Congratulations,";
            int textLength1 = (int) g.getFontMetrics().getStringBounds(line1, g).getWidth();
            int x1 = gp.screenWidth / 2 - textLength1 / 2;
            int y1 = gp.screenHeight / 2 + (gp.tileSize * 4);
            g.drawString(line1, x1, y1);

            String line2 = "Captain!";
            int textLength2 = (int) g.getFontMetrics().getStringBounds(line2, g).getWidth();
            int x2 = gp.screenWidth / 2 - textLength2 / 2;
            int y2 = y1 + gp.tileSize + 10;
            g.drawString(line2, x2, y2);

            gp.gameThread=null;
        } else {
            //KEYS DISPLAY
            g.setFont(arial_40);
            g.setColor(Color.white);


            int baseX = gp.tileSize / 2;
            int baseY = gp.tileSize / 2;
            int space = 34;

            ArrayList<KeyDisplay> keys = new ArrayList<>();

            if (gp.player.hasGoldKey > 0) {
                keys.add(new KeyDisplay(goldKeyImage, gp.player.hasGoldKey));
            }
            if (gp.player.hasSilverKey > 0) {
                keys.add(new KeyDisplay(silverKeyImage, gp.player.hasSilverKey));
            }
            if (gp.player.hasEmeraldKey > 0) {
                keys.add(new KeyDisplay(emeraldKeyImage, gp.player.hasEmeraldKey));
            }

            for (int i = 0; i < keys.size(); i++) {
                KeyDisplay keyDisplay = keys.get(i);
                int y = baseY + i * space;
                g.drawImage(keyDisplay.img, baseX, y, gp.tileSize, gp.tileSize, null);
            }

            //TIME
            playTime+= (double) 1 / 60;
            g.drawString("Time:"+dFormat.format(playTime),gp.tileSize*12,58);


            //MESSAGE
            if (MessageOn == true) {
                g.setFont(g.getFont().deriveFont(30f));
                g.drawString(message, gp.tileSize / 2 * 9, gp.tileSize / 2 * 20);
                messageCounter++;


                if (messageCounter > 120) {
                    messageCounter = 0;
                    MessageOn = false;
                    message = "";
                }
            }
        }
    }

}
