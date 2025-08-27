package Main;

import Entity.Entity;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

import static java.lang.Math.round;

public class UI {

    GamePanel gp;
    Graphics2D g2;

    private Font PublicPixel;

    BufferedImage heartFull, heartHalf, heartEmpty;
    BufferedImage bottleFull, bottleHalf, bottleEmpty;
    BufferedImage coin;

    ArrayList<String> message=new ArrayList<>();
    ArrayList<Integer> messageID=new ArrayList<>();
    public String[] controlLabels = {"Move Up","Move Down","Move Left","Move Right", "Attack", "Shoot", "Interact", "Inventory","Equip/Unequip", "Options", "Back"};
    public String[] controlKeys = {"W","S","A","R", "Left Click", "Right Click", "E", "TAB","Enter", "ESC", ""};
    private int controlScrollOffset = 0;


    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0;

    public int slotCol=0;
    public int tradeSlotCol=0;
    public int slotRow=0;
    public int tradeSlotRow=0;
    public int subState=0;
    int counter=0;

    public Entity trade;

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

        //ALCOHOL
        Entity bottle = new Obj_Alcohol(gp);
        bottleFull = bottle.image1;
        bottleHalf = bottle.image2;
        bottleEmpty = bottle.image3;

        //COIN
        Entity coinI = new Obj_Coin(gp);
        coin=coinI.down1;
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

        //OPTIONS STATE
        if(gp.gameState == gp.optionState) {
            drawOptionsScreen();
        }

        //DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }

        //CHARACTER STATE
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory(gp.player,true);
        }

        //GAME OVER STATE
        if(gp.gameState==gp.gameOverState){
            drawGameOverScreen();
        }

        //TRANSITION FX
        if(gp.gameState == gp.transitionState){
            drawTransitionFX();
        }

        //TRADE STATE
        if(gp.gameState == gp.tradeState){
            drawTradeScreen();
        }
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

        x += gp.tileSize;
        y += gp.tileSize;

        int availableWidth = width - (gp.tileSize * 2);

        ArrayList<String> wrappedLines = wrapText(currentDialogue, availableWidth);

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

            //Confirmation
            g2.setFont(getPublicPixel().deriveFont(12F));
            text = "Press ENTER to confirm";
            x = getXforCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);


        }else if (titleScreenState == 1) {
            gp.music.stop();
            gp.player.setDefaultValues();
            gp.player.setItems();
            gp.gameState = gp.playState;
            gp.music.playMusic(15);
        }

    }

    public void drawCharacterScreen(){
        final int frameX=gp.tileSize*2;
        final int frameY=gp.tileSize;
        final int frameWidth=gp.tileSize*6;
        final int frameHeight=gp.tileSize*11;
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
        textY+=lineHeight*2;
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

        textY+=lineHeight;
        if(gp.player.currentWeapon!=null) {
            g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 5, null);
        }
            textY+=gp.tileSize;
        if(gp.player.currentShield!=null) {
            g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 5, null);
        }
    }

    public void drawOptionsScreen(){
        g2.setColor(Color.white);
        g2.setFont(getPublicPixel());

        int frameX=gp.tileSize*6;
        int frameY=gp.tileSize;
        int frameWidth=gp.tileSize*9;
        int frameHeight=gp.tileSize*11;

        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        switch(subState){
            case 0: optionsTop(frameX,frameY); break;
            case 1: optionsControl(frameX,frameY); break;
            case 2: optionQuitGame(frameX,frameY); break;
        }

        gp.keyHandler.enterPressed=false;
    }

    public void drawGameOverScreen(){
        g2.setColor(new Color(0,0,0,200));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        int frameX;
        int frameY=gp.screenHeight/4;

        g2.setFont(getPublicPixel().deriveFont(64F));
        String text="Game Over";
        g2.setColor(Color.black);
        frameX=getXforCenteredText(text);
        g2.drawString(text,frameX,frameY);
        g2.setColor(Color.white);
        g2.drawString(text,frameX-4,frameY-4);

        g2.setFont(getPublicPixel().deriveFont(24F));
        text="Ye drank too much... or not enough.";
        g2.setColor(Color.black);
        frameX=getXforCenteredText(text);
        g2.drawString(text,frameX,frameY+gp.tileSize);
        g2.setColor(Color.white);
        g2.drawString(text,frameX-4,frameY+gp.tileSize-4);


        g2.setFont(getPublicPixel().deriveFont(15F));
        text="Hoist yer boots and TRY AGAIN, ye stubborn sea dog!";
        g2.setColor(Color.black);
        frameY=gp.screenHeight-gp.tileSize*3;
        frameX=getXforCenteredText(text);
        g2.drawString(text,frameX,frameY);
        g2.setColor(Color.white);
        g2.drawString(text,frameX-4,frameY-4);
        if(commandNum==0){
            g2.drawImage(gp.player.rightImages[0], frameX - 55, frameY - 30, gp.tileSize, gp.tileSize, null);
            g2.drawImage(gp.player.leftImages[0], frameX-5+g2.getFontMetrics().stringWidth(text), frameY-30, gp.tileSize, gp.tileSize, null);
            if(gp.keyHandler.enterPressed){
                gp.music.stop();
                gp.retry();
                gp.gameState=gp.playState;
                gp.music.playMusic(15);
            }
        }

        g2.setFont(getPublicPixel().deriveFont(15F));
        text="BACK to the tavern’s map, where all bad journeys begin.";
        g2.setColor(Color.black);
        frameY=gp.screenHeight-gp.tileSize*2;
        frameX=getXforCenteredText(text);
        g2.drawString(text,frameX,frameY);
        g2.setColor(Color.white);
        g2.drawString(text,frameX-4,frameY-4);
        if(commandNum==1){
            g2.drawImage(gp.player.rightImages[0], frameX - 55, frameY - 30, gp.tileSize, gp.tileSize, null);
            g2.drawImage(gp.player.leftImages[0], frameX-5+g2.getFontMetrics().stringWidth(text), frameY-30, gp.tileSize, gp.tileSize, null);
        if(gp.keyHandler.enterPressed){
            gp.music.stop();
            commandNum=0;
            titleScreenState=0;
           gp.setupGame();
        }
        }
        gp.keyHandler.enterPressed=false;
    }

    public void drawTransitionFX(){
        counter++;
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        if(counter>=50){
            counter=0;
            gp.gameState=gp.dialogueState;
            gp.ui.currentDialogue = "Shiver my timbers! One blink I be here, next blink I be lost… hope there be rum where I land!";
            gp.currentMap=gp.eventHandler.tempMap;
            gp.player.x=gp.tileSize*gp.eventHandler.tempCol;
            gp.player.y=gp.tileSize*gp.eventHandler.tempRow;
            gp.eventHandler.previousEventX=gp.player.x;
            gp.eventHandler.previousEventY=gp.player.y;
        }
    }

    public void drawTradeScreen(){
        switch (subState){
            case 0: tradeSelect(); break;
            case 1: tradeBuy(); break;
            case 2: tradeSell(); break;
        }
        gp.keyHandler.enterPressed=false;
    }

    public void tradeSelect(){
        drawDialogueScreen();

        int x=gp.tileSize*15;
        int y=gp.tileSize*6;
        int width=gp.tileSize*4;
        int height=gp.tileSize*3;
        drawSubWindow(x,y,width,height);

        String text="BUY";
        x+=gp.tileSize+10;
        y+= gp.tileSize+10;
        g2.drawString(text,x+5,y);
        if(commandNum==0){
            g2.drawImage(gp.player.rightImages[0], x - 55, y - 30, gp.tileSize, gp.tileSize, null);
            g2.drawImage(gp.player.leftImages[0], x+g2.getFontMetrics().stringWidth(text)+gp.tileSize/2, y-30, gp.tileSize, gp.tileSize, null);
            if(gp.keyHandler.enterPressed){
                subState=1;
            }
        }
        y+=gp.tileSize;
        g2.drawString("SELL",x,y);
        if(commandNum==1){
            g2.drawImage(gp.player.rightImages[0], x - 55, y - 30, gp.tileSize, gp.tileSize, null);
            g2.drawImage(gp.player.leftImages[0], x+g2.getFontMetrics().stringWidth(text)+gp.tileSize/2, y-30, gp.tileSize, gp.tileSize, null);
            if(gp.keyHandler.enterPressed){
                subState=2;
            }
        }
    }
    public void tradeBuy(){
        drawInventory(gp.player,false);
        drawInventory(trade,true);

        //Coins
        int x=gp.tileSize*13;
        int y=gp.tileSize*10;
        int width=gp.tileSize*6;
        int height=gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.setFont(getPublicPixel().deriveFont(20f));
        g2.drawString("Coins: " + gp.player.coin,x+24,y+60);

        //BUY
        int itemIndex=getItemIndexSlot(tradeSlotCol,tradeSlotRow);
        if(itemIndex<trade.inventory.size()){
            x=(int)(gp.tileSize*5.5);
            y= gp.tileSize*6;
            width= (int) (gp.tileSize*2.5);
            height=gp.tileSize;
            drawSubWindow(x,y,width,height);
            g2.drawImage(coin,x+12,y+16,16,16,null);
            int price=trade.inventory.get(itemIndex).value;
            String text=""+price;
            if(price > 0){
                g2.drawString(text,x+getXAlignToRightText(text,width)-10,y+34);
            }
            if(gp.keyHandler.enterPressed){
                if(trade.inventory.get(itemIndex).value>gp.player.coin){
                    gp.keyHandler.previousGameState = gp.gameState;
                    commandNum=0;
                    subState=0;
                    gp.gameState=gp.dialogueState;
                    currentDialogue="Har har! Yer pockets be emptier than a sober tavern. Come back with more gold, ye stingy barnacle!";

                }
                else if(gp.player.inventory.size()==gp.player.maxInventorySize){
                    gp.keyHandler.previousGameState = gp.gameState;
                    subState=0;
                    commandNum=0;
                    gp.gameState=gp.dialogueState;
                    currentDialogue="Arrr, yer bag be fuller than me belly after ten barrels o’ rum! Toss some junk afore ye buy more.";
                }else{
                    gp.player.coin-=trade.inventory.get(itemIndex).value;
                    gp.player.inventory.add(trade.inventory.get(itemIndex));
                }
            }
        }
    }
    public void tradeSell(){
        drawInventory(gp.player,true);
        //Coins
        int x=gp.tileSize*2;
        int y=gp.tileSize;
        int width=gp.tileSize*6;
        int height=gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.setFont(getPublicPixel().deriveFont(20f));
        g2.drawString("Coins: " + gp.player.coin,x+24,y+60);

        //SELL
        int itemIndex=getItemIndexSlot(slotCol,slotRow);
        if(itemIndex<gp.player.inventory.size()){
            x=(int)(gp.tileSize*16.5);
            y= gp.tileSize*6;
            width= (int) (gp.tileSize*2.5);
            height=gp.tileSize;
            drawSubWindow(x,y,width,height);
            g2.drawImage(coin,x+12,y+16,16,16,null);
            int price=gp.player.inventory.get(itemIndex).value/4;
            String text=""+price;
            if(price > 0){
                g2.drawString(text,x+getXAlignToRightText(text,width)-10,y+34);
            }
            if(gp.keyHandler.enterPressed){
                if(gp.player.inventory.get(itemIndex)==gp.player.currentWeapon || gp.player.inventory.get(itemIndex)==gp.player.currentShield
                ||gp.player.inventory.get(itemIndex)==gp.player.currentHelmet || gp.player.inventory.get(itemIndex)==gp.player.currentChest
                || gp.player.inventory.get(itemIndex)==gp.player.currentBoots){
                    gp.keyHandler.previousGameState = gp.gameState;
                    commandNum=0;
                    subState=0;
                    gp.gameState=gp.dialogueState;
                    currentDialogue="Arrr, ye can’t sell the steel on yer back, ye drunken fool! Unequip it first!";
                }else{
                    gp.player.inventory.remove(itemIndex);
                    gp.player.coin+=price;
                }
            }
        }
    }

    public void optionsTop(int frameX,int frameY){
        int textX;
        int textY;

        String text="Options";
        textX=getXforCenteredText(text);
        textY=frameY+gp.tileSize;
        g2.drawString(text,textX,textY);

        //MUSIC
        textX=frameX+gp.tileSize;
        textY+= (int) (gp.tileSize*1.5);
        text="Music";
        g2.drawString(text,textX,textY);
        if(commandNum==0){
            g2.drawImage(gp.player.rightImages[0], textX-45, textY-30, gp.tileSize, gp.tileSize, null);
            g2.drawImage(gp.player.leftImages[0], textX-5+g2.getFontMetrics().stringWidth(text), textY-30, gp.tileSize, gp.tileSize, null);
        }

        //SE
        textY+=(int) (gp.tileSize*1.5);
        text="SE";
        g2.drawString(text,textX,textY);
            if(commandNum==1) {
                g2.drawImage(gp.player.rightImages[0], textX - 45, textY - 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(gp.player.leftImages[0], textX-5+g2.getFontMetrics().stringWidth(text), textY-30, gp.tileSize, gp.tileSize, null);
            }

        //CONTROL
        textY+=(int) (gp.tileSize*1.5);
            text="Control";
        g2.drawString(text,textX,textY);
        if(commandNum==2) {
            g2.drawImage(gp.player.rightImages[0], textX - 45, textY - 30, gp.tileSize, gp.tileSize, null);
            g2.drawImage(gp.player.leftImages[0], textX-5+g2.getFontMetrics().stringWidth(text), textY-30, gp.tileSize, gp.tileSize, null);
            if(gp.keyHandler.enterPressed) {
                subState = 1;
                commandNum = 0;
                controlScrollOffset=0;
            }
        }

        //QUIT GAME
        textY+=(int) (gp.tileSize*1.5);
        text="Quit Game";
        g2.drawString(text,textX,textY);
        if(commandNum==3) {
            g2.drawImage(gp.player.rightImages[0], textX - 45, textY - 30, gp.tileSize, gp.tileSize, null);
            g2.drawImage(gp.player.leftImages[0], textX-5+g2.getFontMetrics().stringWidth(text), textY-30, gp.tileSize, gp.tileSize, null);
            if(gp.keyHandler.enterPressed) {
                subState = 2;
                commandNum = 0;
            }
        }

        //BACK
        textY+=gp.tileSize*3;
        text="Back";
        g2.drawString(text,textX,textY);
        if(commandNum==4) {
            g2.drawImage(gp.player.rightImages[0], textX - 45, textY - 30, gp.tileSize, gp.tileSize, null);
            g2.drawImage(gp.player.leftImages[0], textX-5+g2.getFontMetrics().stringWidth(text), textY-30, gp.tileSize, gp.tileSize, null);
            if(gp.keyHandler.enterPressed) {
                gp.gameState=gp.keyHandler.previousGameState;
                commandNum = 0;
            }
        }

        g2.setStroke(new BasicStroke(3));

        //MUSIC VOLUME
        textX=frameX+(int)(gp.tileSize*5.5);
        textY=frameY+gp.tileSize*2;
        g2.drawRect(textX,textY,120,24);
        int volumeWidth=24*gp.music.volumeScale;
        g2.fillRect(textX,textY,volumeWidth,24);

        //SE VOLUME
        textY+=(int) (gp.tileSize*1.5);
        g2.drawRect(textX,textY,120,24);
        volumeWidth=24*gp.se.volumeScale;
        g2.fillRect(textX,textY,volumeWidth,24);

        gp.config.saveConfig();

    }

    public void optionsControl(int frameX, int frameY){
        int textX;
        int textY;

        String text = "Options";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        g2.setFont(getPublicPixel().deriveFont(12F));

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;

        int availableHeight = gp.tileSize * 9;
        int lineHeight = gp.tileSize;
        int maxVisibleLines = availableHeight / lineHeight;

        if (commandNum < controlScrollOffset) {
            controlScrollOffset = commandNum;
        } else if (commandNum >= controlScrollOffset + maxVisibleLines) {
            controlScrollOffset = commandNum - maxVisibleLines + 1;
        }

        int startIndex = controlScrollOffset;
        int endIndex = Math.min(startIndex + maxVisibleLines, controlLabels.length);

        for(int i = startIndex; i < endIndex; i++){
            int displayIndex = i - startIndex;
            int currentY = textY + (displayIndex * lineHeight);

            g2.drawString(controlLabels[i], textX, currentY);

            if(commandNum == i) {
                g2.drawImage(gp.player.rightImages[0], textX - 45, currentY - 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(gp.player.leftImages[0], textX-5+g2.getFontMetrics().stringWidth(controlLabels[i]), currentY-30, gp.tileSize, gp.tileSize, null);

                if(i == controlLabels.length-1 && gp.keyHandler.enterPressed) {
                    subState = 0;
                    commandNum = 0;
                    controlScrollOffset = 0;
                }
            }

            if(i < controlKeys.length - 1) {
                int keyX = getXAlignToRightText(controlKeys[i],frameX+gp.tileSize*8);
                g2.drawString(controlKeys[i], keyX, currentY);
            }
        }

        drawScrollIndicators(frameX, frameY, startIndex, endIndex);
    }

    private void drawScrollIndicators(int frameX, int frameY, int startIndex, int endIndex) {
        int indicatorX = frameX + (gp.tileSize * 8);

        if (startIndex > 0) {
            int arrowY = (int) (frameY + (gp.tileSize * 1.5));
            g2.drawString("▲", indicatorX, arrowY);
        }

        if (endIndex < controlLabels.length) {
            int arrowY = (int) (frameY + (gp.tileSize * 10.5));
            g2.drawString("▼", indicatorX, arrowY);
        }
    }

    private void optionQuitGame(int frameX, int frameY) {
        int textX=frameX+gp.tileSize;
        int textY=frameY+gp.tileSize*2;

        g2.setFont(getPublicPixel().deriveFont(12F));
        String text="Abandon ship, ye coward!\nEven the rum can’t save you.\nAre you sure captain?";
        int availableWidth = frameX + (gp.tileSize * 8);
        ArrayList<String> wrappedLines = wrapText(text, availableWidth);
        for (String line : wrappedLines) {
            g2.drawString(line, textX, textY);
            textY += gp.tileSize;
        }

        g2.setFont(getPublicPixel().deriveFont(24F));

        text="No";
        textX=getXforCenteredText(text);
        textY+=gp.tileSize*4;
        g2.drawString(text, textX, textY);
        if(commandNum == 0) {
            g2.drawImage(gp.player.rightImages[0], textX - 45, textY - 30, gp.tileSize, gp.tileSize, null);
            g2.drawImage(gp.player.leftImages[0], textX-5+g2.getFontMetrics().stringWidth(text), textY-30, gp.tileSize, gp.tileSize, null);
            if(gp.keyHandler.enterPressed) {
                subState = 0;
                commandNum = 0;
                gp.gameState=gp.optionState;
            }
        }

        text="Yes";
        textX=getXforCenteredText(text);
        textY+=gp.tileSize;
        g2.drawString(text, textX, textY);
        if(commandNum == 1) {
            g2.drawImage(gp.player.rightImages[0], textX - 45, textY - 30, gp.tileSize, gp.tileSize, null);
            g2.drawImage(gp.player.leftImages[0], textX-5+g2.getFontMetrics().stringWidth(text), textY-30, gp.tileSize, gp.tileSize, null);
            if(gp.keyHandler.enterPressed) {
                System.exit(0);
            }
        }

    }

    public void drawInventory(Entity entity,boolean cursor){

        int frameX;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
        int slotTCol;
        int slotTRow;

        if(entity==gp.player) {

            slotTCol=slotCol;
            slotTRow=slotRow;

            frameX = gp.tileSize * 13;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
        }else{
            slotTCol=tradeSlotCol;
            slotTRow=tradeSlotRow;

            frameX = gp.tileSize * 2;
        }
         drawSubWindow(frameX,frameY,frameWidth,frameHeight);

         //slot
         final int slotXStart=frameX+20;
         final int slotYStart=frameY+20;
         int slotX=slotXStart;
         int slotY=slotYStart;
         int slotSize=gp.tileSize+3;

         //draw items
        for(int i=0;i<entity.inventory.size();i++) {

            //equip cursor
            if (entity==gp.player && entity.inventory.get(i) == entity.currentWeapon ||
                    entity.inventory.get(i) == entity.currentShield ||
                    entity.inventory.get(i) == entity.currentBoots ||
                    entity.inventory.get(i) == entity.currentChest ||
                    entity.inventory.get(i) == entity.currentHelmet) {
                g2.setColor(new Color(98, 189, 38, 139));
                g2.fillRoundRect(slotX,slotY,gp.tileSize,gp.tileSize,10,10);
            }
            if (entity.inventory.get(i) != null) {
                g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
                slotX += slotSize;
                if (i % 5 == 4) {
                    slotX = slotXStart;
                    slotY += slotSize;
                }
            }
        }

         //cursor
        if(cursor) {
            int cursorX=slotXStart+(slotSize*slotTCol);
            int cursorY=slotYStart+(slotSize*slotTRow);
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

            int itemIndex=getItemIndexSlot(slotTCol,slotTRow);

            if(itemIndex<entity.inventory.size()){

                drawSubWindow(frameX,dFrameY, frameWidth,dFrameHeight);

                ArrayList<String> wrappedLines = wrapText(entity.inventory.get(itemIndex).itemDescription, frameWidth);

                // Draw each wrapped line
                for (String line : wrappedLines) {
                    g2.drawString(line, textX, textY);
                    textY += 40;
                }
            }
        }
    }

    public int getItemIndexSlot(int slotC,int slotR){
        return slotC+(slotR*5);
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
        int playerX_1 = getXforCenteredText(text) - gp.tileSize * 2;
        int playerX_2 = getXforCenteredText(text) + g2.getFontMetrics().stringWidth(text)-10;
        int playerY = gp.screenHeight / 4 + gp.tileSize * 2 + gp.tileSize * index - 20;
        g2.drawImage(gp.player.rightImages[0], playerX_1, playerY, gp.tileSize * 2, gp.tileSize * 2, null);
        g2.drawImage(gp.player.leftImages[0], playerX_2, playerY, gp.tileSize * 2, gp.tileSize * 2, null);
    }

    public void drawPlayerLife() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        //HEARTS
        while (round((float)(i)) < gp.player.maxHealth / 2) {
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