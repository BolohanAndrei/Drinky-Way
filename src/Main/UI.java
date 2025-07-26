package Main;

import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;

    Font PublicPixel;

    BufferedImage heartFull, heartHalf, heartEmpty;
    BufferedImage bottleFull, bottleHalf, bottleEmpty;

    public boolean MessageOn = false;
    public String message = "";
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0;

    //CONSTRUCTOR
    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/res/Fonts/PublicPixel-rv0pA.ttf");
            if (is != null) {
                PublicPixel = Font.createFont(Font.TRUETYPE_FONT, is);
                PublicPixel = PublicPixel.deriveFont(24F);
            } else {
                System.err.println("Font file not found!");
                PublicPixel = new Font("Arial", Font.BOLD, 24);
            }
        } catch (Exception e) {
            e.printStackTrace();
            PublicPixel = new Font("Arial", Font.BOLD, 24);
        }

        //HEARTS
        SuperObject heart = new Obj_Heart(gp);
        heartFull = heart.image1;
        heartHalf = heart.image2;
        heartEmpty = heart.image3;

        SuperObject bottle = new Obj_Alcohol(gp);
        bottleFull = bottle.image1;
        bottleHalf = bottle.image2;
        bottleEmpty = bottle.image3;
    }

    public void showMessage(String text) {
        message = text;
        MessageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(PublicPixel);
        g2.setColor(Color.white);

        //TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        //PLAT STATE
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawDrunkLevel();
        }

        //PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawDrunkLevel();
            drawPauseScreen();
        }

        //DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDrunkLevel();
            drawDialogueScreen();

        }
    }

    public void drawPauseScreen() {
        g2.setFont(PublicPixel.deriveFont(48F));
        g2.setColor(Color.white);
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen() {
        //WINDOW
        int x, y, width, height;

        x = gp.tileSize * 2;
        y = gp.tileSize / 2;
        width = gp.screenWidth - (gp.tileSize * 4);
        height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));

        //TEXT - Using automatic text wrapping
        x += gp.tileSize;
        y += gp.tileSize;

        // Calculate available width for text (subtract padding)
        int availableWidth = width - (gp.tileSize * 2);

        // Get wrapped lines
        ArrayList<String> wrappedLines = wrapText(currentDialogue, availableWidth);

        // Draw each wrapped line
        for (String line : wrappedLines) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawTitleScreen() {

        if (titleScreenState == 0) {

            //GAME NAME + PLAYER IMAGE
            g2.setFont(PublicPixel.deriveFont(68F));
            String text = "Drinky Way";
            int x = getXforCenteredText(text);
            int y = gp.screenHeight / 4;
            g2.drawString(text, x, y);

            //MENU GAME
            //New Game
            g2.setFont(PublicPixel.deriveFont(48F));
            text = "New Game";
            x = getXforCenteredText(text);
            y += gp.tileSize * 3;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                drawMenuPlayerImages(text, 0);
            }
            //Load Game
            text = "Load Game";
            x = getXforCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                drawMenuPlayerImages(text, 2);
            }

            //Quit Game
            text = "Quit Game";
            x = getXforCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                drawMenuPlayerImages(text, 4);
            }
        } else if (titleScreenState == 1) {
            //Class selection screen
            g2.setColor(Color.white);
            g2.setFont(PublicPixel.deriveFont(30F));
            String text = "Select your class";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 2;
            g2.drawString(text, x, y);

            text = "Pirate";
            x = getXforCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                drawMenuPlayerImages(text, -2);
            }

            text = "Sailor";
            x = getXforCenteredText(text);
            y += gp.tileSize*2;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                drawMenuPlayerImages(text, 0);
            }

            text = "Thief";
            x = getXforCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                drawMenuPlayerImages(text, 2);
            }

            text = "Back";
            x = getXforCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if (commandNum == 3) {
                drawMenuPlayerImages(text, 4);
            }
        }

    }

    public ArrayList<String> wrapText(String text, int maxWidth) {
        ArrayList<String> wrappedLines = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return wrappedLines;
        }

        if (g2.getFontMetrics().stringWidth(text) <= maxWidth) {
            wrappedLines.add(text);
            return wrappedLines;
        }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;

            if (g2.getFontMetrics().stringWidth(testLine) > maxWidth) {
                if (currentLine.length() > 0) {
                    wrappedLines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    String brokenWord = breakLongWord(word, maxWidth);
                    wrappedLines.add(brokenWord);
                    currentLine = new StringBuilder();
                }
            } else {
                currentLine = new StringBuilder(testLine);
            }
        }

        if (currentLine.length() > 0) {
            wrappedLines.add(currentLine.toString());
        }

        return wrappedLines;
    }

    private String breakLongWord(String word, int maxWidth) {
        if (g2.getFontMetrics().stringWidth(word) <= maxWidth) {
            return word;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            String testString = result.toString() + word.charAt(i);
            if (g2.getFontMetrics().stringWidth(testString) > maxWidth) {
                break;
            }
            result.append(word.charAt(i));
        }

        return result.toString();
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 175);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255, 200);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawMenuPlayerImages(String text, int index) {
        int playerX_1 = getXforCenteredText(text) - gp.tileSize * 2 - 10;
        int playerX_2 = getXforCenteredText(text) + g2.getFontMetrics().stringWidth(text) + 10;
        int playerY = gp.screenHeight / 4 + gp.tileSize * 2 + gp.tileSize * index - 20;
        g2.drawImage(gp.player.down1, playerX_1, playerY, gp.tileSize * 2, gp.tileSize * 2, null);
        g2.drawImage(gp.player.down2, playerX_2, playerY, gp.tileSize * 2, gp.tileSize * 2, null);
    }

    public void drawPlayerLife() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        //HEARTS
        while (i < gp.player.maxHealth / 2) {
            g2.drawImage(heartEmpty, x, y, null);
            i++;
            x += gp.tileSize;
        }

        //RESET
         x = gp.tileSize / 2;
         y = gp.tileSize / 2;
         i = 0;

         //FULL HEARTS
        while (i < gp.player.health) {
            g2.drawImage(heartHalf, x, y, null);
            i++;
            if(i<gp.player.health){
                g2.drawImage(heartFull, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

    }

    public void drawDrunkLevel()  {
        int x = gp.tileSize / 2;
        int y = gp.tileSize*2- gp.tileSize / 2;
        int i = 0;

        //HEARTS
        while (i < gp.player.maxDrunk / 2) {
            g2.drawImage(bottleEmpty, x, y, null);
            i++;
            x += gp.tileSize;
        }

        //RESET
        x = gp.tileSize / 2;
        y = gp.tileSize*2- gp.tileSize / 2;
        i = 0;

        //FULL HEARTS
        while (i < gp.player.drunk) {
            g2.drawImage(bottleHalf, x, y, null);
            i++;
            if(i<gp.player.drunk){
                g2.drawImage(bottleFull, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (gp.screenWidth / 2) - (length / 2);
    }
}