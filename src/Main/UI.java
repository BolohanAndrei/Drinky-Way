package Main;

import Entity.Entity;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;

    private Font PublicPixel;

    BufferedImage heartFull, heartHalf, heartEmpty;
    BufferedImage bottleFull, bottleHalf, bottleEmpty;

    ArrayList<String> message=new ArrayList<>();
    ArrayList<Integer> messageID=new ArrayList<>();

    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0;

    public int slotCol=0;
    public int slotRow=0;

    //CONSTRUCTOR
    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/res/Fonts/PublicPixel-rv0pA.ttf");
            if (is != null) {
                PublicPixel = Font.createFont(Font.TRUETYPE_FONT, is);
                PublicPixel = getPublicPixel().deriveFont(24F);
            } else {
                System.err.println("Font file not found!");
                PublicPixel = new Font("Arial", Font.BOLD, 24);
            }
        } catch (Exception e) {
            e.getStackTrace();
            PublicPixel = new Font("Arial", Font.BOLD, 24);
        }

        //HEARTS
        Entity heart = new Obj_Heart(gp);
        heartFull = heart.image1;
        heartHalf = heart.image2;
        heartEmpty = heart.image3;

        Entity bottle = new Obj_Alcohol(gp);
        bottleFull = bottle.image1;
        bottleHalf = bottle.image2;
        bottleEmpty = bottle.image3;
    }

    public void addMessage(String text){
        message.add(text);
        messageID.add(0);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(getPublicPixel());
        g2.setColor(Color.white);

        //TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        //PLAT STATE
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawDrunkLevel();
            drawMessage();
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

        //CHARACTER STATE
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory();
        }
    }

    public void drawPauseScreen() {
        g2.setFont(getPublicPixel().deriveFont(48F));
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
            g2.setFont(getPublicPixel().deriveFont(68F));
            String text = "Drinky Way";
            int x = getXforCenteredText(text);
            int y = gp.screenHeight / 4;
            g2.drawString(text, x, y);

            //MENU GAME
            //New Game
            g2.setFont(getPublicPixel().deriveFont(48F));
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
        }else if (titleScreenState == 1) {
            gp.gameState = gp.playState;
            gp.sound.stopMusic();
            gp.sound.playMusic(15);
        }

    }

    public void drawCharacterScreen(){
        final int frameX=gp.tileSize;
        final int frameY=gp.tileSize;
        final int frameWidth=gp.tileSize*5;
        final int frameHeight=gp.tileSize*10;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        //text
        g2.setColor(Color.white);
        g2.setFont(getPublicPixel().deriveFont(12F));

        int textX=frameX+20;
        int textY=frameY+gp.tileSize;
        final int lineHeight=30;

        //Names
        textY=statusDraw("Level",textX,textY,lineHeight);
        textY=statusDraw("Exp",textX,textY,lineHeight);
        textY=statusDraw("Exp to up",textX,textY,lineHeight);
        textY=statusDraw("Life",textX,textY,lineHeight);
        textY=statusDraw("Alcohol",textX,textY,lineHeight);
        textY=statusDraw("Armour",textX,textY,lineHeight);
        textY=statusDraw("Attack",textX,textY,lineHeight);
        textY=statusDraw("Strength",textX,textY,lineHeight);
        textY=statusDraw("Dexterity",textX,textY,lineHeight);
        textY=statusDraw("Coins",textX,textY,lineHeight);
        textY+=lineHeight;
        textY=statusDraw("Weapon",textX,textY,lineHeight);
        textY+=lineHeight/2;
        statusDraw("Shield",textX,textY,lineHeight);


        //Values
        int tailX=(frameX+frameWidth)-30;
        textY=frameY+gp.tileSize;
        String value;
        value=String.valueOf(gp.player.level);
        textY=valuesDraw(value, tailX,textY,lineHeight);
        value=String.valueOf(gp.player.exp);
        textY=valuesDraw(value, tailX,textY,lineHeight);
        value=String.valueOf(gp.player.nextLevelExp-gp.player.exp);
        textY=valuesDraw(value, tailX,textY,lineHeight);
        value= gp.player.health + "/" + gp.player.maxHealth;
        textY=valuesDraw(value, tailX,textY,lineHeight);
        value= gp.player.drinkPercent + "/" + gp.player.maxDrinkPercent + "%";
        textY=valuesDraw(value, tailX,textY,lineHeight);
        value=String.valueOf(gp.player.defense);
        textY=valuesDraw(value, tailX,textY,lineHeight);
        value=String.valueOf(gp.player.attack);
        textY=valuesDraw(value, tailX,textY,lineHeight);
        value=String.valueOf(gp.player.strength);
        textY=valuesDraw(value, tailX,textY,lineHeight);
        value=String.valueOf(gp.player.dexterity);
        textY=valuesDraw(value, tailX,textY,lineHeight);
        value=String.valueOf(gp.player.coin);
        textY=valuesDraw(value, tailX,textY,lineHeight);

        if(gp.player.currentWeapon!=null) {
            g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 5, null);
        }
            textY+=gp.tileSize;
        if(gp.player.currentShield!=null) {
            g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 5, null);
        }
    }

    public void drawInventory(){
        //frame
         int frameX=gp.tileSize*9;
         int frameY=gp.tileSize;
         int frameWidth=gp.tileSize*6;
         int frameHeight=gp.tileSize*5;
         drawSubWindow(frameX,frameY,frameWidth,frameHeight);

         //slot
         final int slotXStart=frameX+20;
         final int slotYStart=frameY+20;
         int slotX=slotXStart;
         int slotY=slotYStart;
         int slotSize=gp.tileSize+3;

         //draw items
        for(int i=0;i<gp.player.inventory.size();i++) {

            //equip cursor
            if (gp.player.inventory.get(i) == gp.player.currentWeapon ||
                    gp.player.inventory.get(i) == gp.player.currentShield ||
                    gp.player.inventory.get(i) == gp.player.currentBoots ||
                    gp.player.inventory.get(i) == gp.player.currentChest ||
                    gp.player.inventory.get(i) == gp.player.currentHelmet) {
                g2.setColor(new Color(98, 189, 38, 139));
                g2.fillRoundRect(slotX,slotY,gp.tileSize,gp.tileSize,10,10);
            }
            if (gp.player.inventory.get(i) != null) {
                g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
                slotX += slotSize;
                if (i % 5 == 4) {
                    slotX = slotXStart;
                    slotY += slotSize;
                }
            }
        }

         //cursor
         int cursorX=slotXStart+(slotSize*slotCol);
         int cursorY=slotYStart+(slotSize*slotRow);
         int cursorWidth=gp.tileSize;
         int cursorHeight=gp.tileSize;
        //draw cursor
         g2.setColor(Color.white);
         g2.setStroke(new BasicStroke(3));
         g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight,10,10);

         //description
        int dFrameY=frameY+frameHeight+gp.tileSize;
        int dFrameHeight=gp.tileSize*5;
        //draw description
        int textX= frameX +20;
        int textY=dFrameY+gp.tileSize;
        g2.setFont(getPublicPixel().deriveFont(10F));

        int itemIndex=getItemIndexSlot();

        if(itemIndex<gp.player.inventory.size()){

            drawSubWindow(frameX,dFrameY, frameWidth,dFrameHeight);

            ArrayList<String> wrappedLines = wrapText(gp.player.inventory.get(itemIndex).itemDescription, frameWidth);

            // Draw each wrapped line
            for (String line : wrappedLines) {
                g2.drawString(line, textX, textY);
                textY += 40;
            }
        }


    }

    public int getItemIndexSlot(){
        return slotCol+(slotRow*5);
    }

    public int valuesDraw(String value, int tailX, int textY, int lineHeight){
        int textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX,textY);
        return textY+lineHeight;
    }

    public int statusDraw(String status,int textX,int textY,int lineHeight){
        g2.drawString(status,textX,textY);
        return textY+lineHeight;
    }

    public ArrayList<String> wrapText(String text, int maxWidth) {
        ArrayList<String> wrappedLines = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return wrappedLines;
        }

        // First split by manual line breaks (\n)
        String[] manualLines = text.split("\\n");

        for (String line : manualLines) {
            // Check if this line needs wrapping
            if (g2.getFontMetrics().stringWidth(line) <= maxWidth) {
                wrappedLines.add(line);
            } else {
                // Apply word wrapping to this line
                String[] words = line.split(" ");
                StringBuilder currentLine = new StringBuilder();

                for (String word : words) {
                    String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;

                    if (g2.getFontMetrics().stringWidth(testLine) > maxWidth) {
                        if (!currentLine.isEmpty()) {
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

                if (!currentLine.isEmpty()) {
                    wrappedLines.add(currentLine.toString());
                }
            }
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

    public void drawMessage(){
        int messageX = gp.tileSize;
        int messageY = gp.screenHeight - gp.tileSize * 2;
        g2.setFont(getPublicPixel().deriveFont(10F));

        int i = 0;
        while(i < message.size()){
            String txt = message.get(i);
            if(txt != null){
                g2.setColor(Color.white);
                g2.drawString(txt, messageX, messageY);
                int counter = messageID.get(i) + 1;
                messageID.set(i, counter);
                messageY += gp.tileSize;
                if(counter > 90){
                    message.remove(i);
                    messageID.remove(i);
                    continue;
                }
            }
            i++;
        }
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (gp.screenWidth / 2) - (length / 2);
    }
    public int getXAlignToRightText(String text,int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX-length;
    }

    public Font getPublicPixel() {
        return PublicPixel;
    }
}