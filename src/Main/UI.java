package Main;

import Entity.Entity;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;

    private Font PublicPixel;

    BufferedImage heartFull, heartHalf, heartEmpty;
    BufferedImage bottleFull, bottleHalf, bottleEmpty;
    BufferedImage coin;

    ArrayList<String> message=new ArrayList<>();
    ArrayList<Integer> messageID=new ArrayList<>();
    public String[] controlLabels = {"Move Up","Move Down","Move Left","Move Right","Guard", "Attack", "Shoot", "Interact", "Inventory","Equip/Unequip","Map","Mini Map", "Options", "Back"};
    public String[] controlKeys = {"W","S","A","D","Q", "Left Click", "Right Click", "E", "TAB","Enter","M","N", "ESC", ""};
    private int controlScrollOffset = 0;


    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0;

    public int slotCol=0;
    public int tradeSlotCol=0;
    public int slotRow=0;
    public int tradeSlotRow=0;
    public int chestSlotCol = 0;
    public int chestSlotRow = 0;
    public int subState=0;
    int counter=0;
    public boolean chestFocusPlayer = true;
    public boolean sleepActive=false;
    private int currentSleepPhase = 0; // 0 sleep, 1 wake
    private int phaseFrame = 0;

    public Entity trade; //NPC
    public Entity chest;

    private int optionScrollOffset=0;

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

        cacheFonts();
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

    private static final boolean ENABLE_UI_CACHING = true;

    private Font font10, font12, font15, font20, font24, font48, font64, font68;

    private final java.util.Map<Long, BufferedImage> subWindowCache = new java.util.HashMap<>();

    private static class InventoryCache { String signature; BufferedImage image; int width; int height; }
    private final java.util.IdentityHashMap<Entity, InventoryCache> inventoryCaches = new java.util.IdentityHashMap<>();

    private final StringBuilder sigBuilder = new StringBuilder(256);

    public void addMessage(String text){
        text = gp.drinkSystem.slurIfNeeded(text);
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

        //PLAY STATE
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

        //CHEST STATE
        if(gp.gameState == gp.chestState){
            drawChestScreen();
        }

        //SLEEP STATE
        if(gp.gameState==gp.sleepState){
            drawSleepScreen();
        }

        //SAVE STATE
        if(gp.gameState == gp.saveState){
            drawSaveScreen();
        }

        //AUTO-SAVE MESSAGE
        if(gp.showAutoSaveMessage){
            drawAutoSaveMessage();
        }
    }

    public void drawDialogueScreen() {
        int x, y, width, height;

        x = gp.tileSize * 2;
        y = gp.tileSize / 2;
        width = gp.screenWidth - (gp.tileSize * 4);
        height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(sized(12f));
        if(gp.gameState == gp.tradeState || gp.gameState == gp.chestState){
            drawSubWindow(gp.tileSize*12, (int)(gp.tileSize*4.5), gp.tileSize*7, gp.tileSize);
            g2.drawString("Press ENTER to continue", (int)(gp.tileSize*12.5), (int)(gp.tileSize*5.1));
            drawSubWindow(gp.tileSize*14, gp.tileSize*11, gp.tileSize*5, gp.tileSize);
            g2.drawString("Press E to exit", (int)(gp.tileSize*14.5), (int)(gp.tileSize*11.6));

        }else{
            drawSubWindow(gp.tileSize*13, (int)(gp.tileSize*4.5), gp.tileSize*6, gp.tileSize);
            g2.drawString("Press E to continue", (int)(gp.tileSize*13.5), (int)(gp.tileSize*5.1));
        }

        g2.setFont(sized(20f));

        x += gp.tileSize;
        y += gp.tileSize;

        if(trade.dialogue[trade.dialogueSet][trade.dialogueIndex]!=null){
            currentDialogue = trade.dialogue[trade.dialogueSet][trade.dialogueIndex];
            currentDialogue = gp.drinkSystem.slurIfNeeded(currentDialogue);

            if(gp.keyHandler.ePressed){
                if(gp.gameState == gp.dialogueState){
                    trade.dialogueIndex++;
                    gp.keyHandler.ePressed = false;
                }
            }
        }else{
            trade.dialogueIndex=0;
            if(gp.gameState == gp.dialogueState){
                gp.gameState=gp.playState;
            }
        }

        int availableWidth = width - (gp.tileSize * 2);

        ArrayList<String> wrappedLines = wrapText(currentDialogue, availableWidth);

        for (String line : wrappedLines) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawTitleScreen() {

        if (titleScreenState == 0) {

            g2.setFont(sized(68f));
            String text = "Drinky Way";
            int x = getXforCenteredText(text);
            int y = gp.screenHeight / 4;
            g2.drawString(text, x, y);


            g2.setFont(sized(48f));
            text = "New Game";
            x = getXforCenteredText(text);
            y += gp.tileSize * 3;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                drawMenuPlayerImages(text, 0);
            }
            text = "Load Game";
            x = getXforCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                drawMenuPlayerImages(text, 2);
            }

            text = "Quit Game";
            x = getXforCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                drawMenuPlayerImages(text, 4);
            }

            g2.setFont(sized(12f));
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

        g2.setColor(Color.white);
        g2.setFont(sized(12f));

        int textX=frameX+20;
        int textY=frameY+gp.tileSize;
        final int lineHeight=30;

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

        g2.setFont(sized(64f));
        String text="Game Over";
        g2.setColor(Color.black);
        frameX=getXforCenteredText(text);
        g2.drawString(text,frameX,frameY);
        g2.setColor(Color.white);
        g2.drawString(text,frameX-4,frameY-4);

        g2.setFont(sized(24f));
        text="Ye drank too much... or not enough.";
        g2.setColor(Color.black);
        frameX=getXforCenteredText(text);
        g2.drawString(text,frameX,frameY+gp.tileSize);
        g2.setColor(Color.white);
        g2.drawString(text,frameX-4,frameY+gp.tileSize-4);


        g2.setFont(sized(15f));
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

        g2.setFont(sized(15f));
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
            gp.eventHandler.event.startDialogue(gp.eventHandler.event,1);
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
        trade.dialogueSet=0;
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


        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));
        drawSubWindow(gp.tileSize*13, (int)(gp.tileSize*8.5), gp.tileSize*6, gp.tileSize);
        g2.drawString("Press E to go back", (int)(gp.tileSize*13.5), (int)(gp.tileSize*9.1));
        drawSubWindow(gp.tileSize*13, gp.tileSize*7, gp.tileSize*6, gp.tileSize);
        g2.drawString("Press ENTER to buy", (int)(gp.tileSize*13.5), (int)(gp.tileSize*7.6));

        int x=gp.tileSize*13;
        int y=gp.tileSize*10;
        int width=gp.tileSize*6;
        int height=gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.setFont(getPublicPixel().deriveFont(20f));
        g2.drawString("Coins: " + gp.player.coin,x+24,y+60);


        int itemIndex=getItemIndexSlot(tradeSlotCol,tradeSlotRow);
        if(itemIndex<trade.inventory.size()){
            x=(int)(gp.tileSize*3.7);
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
                    trade.startDialogue(trade,1);

                }
                else{
                    if(gp.player.canObtainItem(trade.inventory.get(itemIndex))){
                        gp.player.coin-=trade.inventory.get(itemIndex).value;
                    }else{
                        subState=0;
                        commandNum=0;
                        trade.startDialogue(trade,2);

                    }
                }
            }
        }
    }
    public void tradeSell(){
        drawInventory(gp.player,true);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));
        drawSubWindow(gp.tileSize*2, gp.tileSize*11, gp.tileSize*6, gp.tileSize);
        g2.drawString("Press E to go back", (int)(gp.tileSize*2.5), (int)(gp.tileSize*11.6));
        drawSubWindow(gp.tileSize*2, (int)(gp.tileSize*9.5), gp.tileSize*6, gp.tileSize);
        g2.drawString("Press ENTER to sell", (int)(gp.tileSize*2.5), (int)(gp.tileSize*10.1));

        int x=gp.tileSize*2;
        int y=gp.tileSize;
        int width=gp.tileSize*6;
        int height=gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.setFont(getPublicPixel().deriveFont(20f));
        g2.drawString("Coins: " + gp.player.coin,x+24,y+60);

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
                    trade.startDialogue(trade,3);
                }else{
                    if(gp.player.inventory.get(itemIndex).amount>1){
                        gp.player.inventory.get(itemIndex).amount--;
                    }else{
                        gp.player.inventory.remove(itemIndex);
                    }
                    gp.player.coin+=price;
                }
            }
        }
    }

    public void drawChestScreen() {
        if(gp.keyHandler.spacePressed){
            chestFocusPlayer = !chestFocusPlayer;
            gp.keyHandler.spacePressed = false;
            gp.se.playSE(19);
        }

        drawInventory(gp.player, chestFocusPlayer);
        drawInventory(chest, !chestFocusPlayer);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));

        if(chestFocusPlayer){
            int bx1 = gp.tileSize*2;
            drawSubWindow(bx1, gp.tileSize*8, gp.tileSize*6, gp.tileSize);
            g2.drawString("Press ENTER to drop", (int)(bx1+gp.tileSize*0.5), (int)(gp.tileSize*8.6));
            drawSubWindow(bx1, (int)(gp.tileSize*9.5), gp.tileSize*6, gp.tileSize);
            g2.drawString("Press SPACE to chg", (int)(bx1+gp.tileSize*0.5), (int)(gp.tileSize*10.1));
            drawSubWindow(bx1, gp.tileSize*11, gp.tileSize*6, gp.tileSize);
            g2.drawString("Press E to exit", (int)(bx1+gp.tileSize*0.5), (int)(gp.tileSize*11.6));
        } else {
            int bx1 = gp.tileSize*13;
            drawSubWindow(bx1, gp.tileSize*8, gp.tileSize*6, gp.tileSize);
            g2.drawString("Press ENTER to pick", (int)(bx1+gp.tileSize*0.5), (int)(gp.tileSize*8.6));
            drawSubWindow(bx1, (int)(gp.tileSize*9.5), gp.tileSize*6, gp.tileSize);
            g2.drawString("Press SPACE to chg", (int)(bx1+gp.tileSize*0.5), (int)(gp.tileSize*10.1));
            drawSubWindow(bx1, gp.tileSize*11, gp.tileSize*6, gp.tileSize);
            g2.drawString("Press E to exit", (int)(bx1+gp.tileSize*0.5), (int)(gp.tileSize*11.6));
        }

        if (gp.keyHandler.enterPressed) {
            if (chest == null) return;
            if (chestFocusPlayer) {
                int itemIndex = getItemIndexSlot(slotCol, slotRow);
                if (itemIndex >= 0 && itemIndex < gp.player.inventory.size()) {
                    Entity item = gp.player.inventory.get(itemIndex);

                    if (item == gp.player.currentWeapon || item == gp.player.currentShield
                            || item == gp.player.currentHelmet || item == gp.player.currentChest
                            || item == gp.player.currentBoots) {
                        gp.keyHandler.previousGameState = gp.gameState;
                        chest.startDialogue(chest,0);
                        chest.down1=chest.image1;
                        } else {
                        if (transferItemToChest(item, itemIndex)) {
                            gp.se.playSE(30);
                        } else {
                            gp.keyHandler.previousGameState = gp.gameState;
                            chest.startDialogue(chest,1);
                            chest.down1=chest.image1;
                        }
                    }
                }
            } else {
                int itemIndex = getItemIndexSlot(chestSlotCol, chestSlotRow);
                if (itemIndex >= 0 && itemIndex < chest.inventory.size()) {
                    Entity item = chest.inventory.get(itemIndex);

                    if (gp.player.canObtainItem(item)) {
                        if (item.amount > 1) {
                            item.amount--;
                        } else {
                            chest.inventory.remove(itemIndex);
                        }
                        gp.se.playSE(30);
                    } else {
                        gp.keyHandler.previousGameState = gp.gameState;
                        chest.startDialogue(chest,2);
                        chest.down1=chest.image1;
                    }
                }
            }
            gp.keyHandler.enterPressed = false;
        }
    }

    public void startSleep(){
        if(sleepActive){return;}
        sleepActive = true;
        counter=0;
        gp.envManager.light.filterAlpha=0f;
        gp.gameState = gp.sleepState;
    }

    public void drawSleepScreen() {
        if (!sleepActive) return;

        final int sleepFrameCount = gp.player.sleep.length;
        final int wakeFrameCount = gp.player.wake.length;
        final int sleepFrameDuration = 8;
        final int wakeFrameDuration = 9;


        if (counter == 0) {
            currentSleepPhase = 0;
            phaseFrame = 0;
        }

        if (currentSleepPhase == 0) {
            int frameIndex = phaseFrame / sleepFrameDuration;
            if (frameIndex >= sleepFrameCount) {
                currentSleepPhase = 1;
                phaseFrame = 0;
            }
        }

        BufferedImage frame;
        if (currentSleepPhase == 0) {
            int frameIndex = Math.min(sleepFrameCount - 1, phaseFrame / sleepFrameDuration);
            frame = gp.player.sleep[frameIndex];
            float t = frameIndex / (float) (sleepFrameCount - 1 == 0 ? 1 : sleepFrameCount - 1);
            gp.envManager.light.filterAlpha = Math.min(1f, t);
        } else {
            int frameIndex = Math.min(wakeFrameCount - 1, phaseFrame / wakeFrameDuration);
            frame = gp.player.wake[frameIndex];
            float t = frameIndex / (float) (wakeFrameCount - 1 == 0 ? 1 : wakeFrameCount - 1);
            gp.envManager.light.filterAlpha = Math.max(0f, 1f - t);
            if (frameIndex == wakeFrameCount - 1 &&
                    (phaseFrame / wakeFrameDuration) >= wakeFrameCount) {
                gp.envManager.light.filterAlpha = 0f;
                counter = 0;
                phaseFrame = 0;
                currentSleepPhase = 0;
                sleepActive = false;
                gp.player.drinkPercent = 99;
                gp.gameState = gp.playState;
                return;
            }
        }

        if (frame == null) {
            frame = gp.player.idle_down;
        }

        gp.player.recenter();
        g2.drawImage(frame, gp.player.screenX, gp.player.screenY, gp.tileSize, gp.tileSize, null);

        phaseFrame++;
        counter++;
    }

    public void drawSaveScreen(){
        g2.setColor(new Color(0,0,0,200));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        int frameX;
        int frameY=gp.screenHeight/6;

        g2.setFont(sized(64f));
        String text="Save Game";
        g2.setColor(Color.black);
        frameX=getXforCenteredText(text);
        g2.drawString(text,frameX,frameY);
        g2.setColor(Color.white);
        g2.drawString(text,frameX-4,frameY-4);

        frameY += 50;
        g2.setFont(sized(24f));
        text="By the seas! The siren's brew heals my wounds… and curses me with sobriety!\nThe siren's brew preserves yer journey...";
        int availableWidth=gp.screenWidth;
        ArrayList<String> wrappedLines = wrapText(text, availableWidth);

        for (String line : wrappedLines) {
            g2.drawString(line, gp.tileSize, frameY);
            frameY += 40;
        }

        g2.setFont(sized(15f));
        text="SAVE and continue yer drunken adventure";
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
                gp.saveload.save();
                gp.gameState=gp.playState;
                gp.se.playSE(12);
            }
        }

        g2.setFont(sized(15f));
        text="BACK to the fountain without saving";
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
                gp.gameState=gp.playState;
                commandNum=0;
            }
        }
        gp.keyHandler.enterPressed=false;
    }

    public void drawAutoSaveMessage(){
        g2.setFont(sized(15f));
        String text = "Game Auto-Saved";

        int x = getXforCenteredText(text);
        int y = gp.tileSize;

        int textWidth = g2.getFontMetrics().stringWidth(text);
        int textHeight = g2.getFontMetrics().getHeight();
        int padding = 10;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x - padding, y - textHeight + 5, textWidth + 2 * padding, textHeight + padding, 10, 10);

        g2.setColor(new Color(255, 255, 255, 200));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x - padding, y - textHeight + 5, textWidth + 2 * padding, textHeight + padding, 10, 10);

        g2.setColor(Color.black);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }

    public void optionsTop(int frameX,int frameY){
        int textX; int textY;
        String header="Options";
        textX=getXforCenteredText(header);
        textY=frameY+gp.tileSize;
        g2.drawString(header,textX,textY);

        java.util.List<String> labels = new java.util.ArrayList<>();
        labels.add("Music");
        labels.add("SE");
        labels.add("Control");
        labels.add("Quit Game");
        labels.add("Back");

        if(commandNum>=labels.size()) commandNum = labels.size()-1;

        int maxVisible = 5;
        if(commandNum < optionScrollOffset) optionScrollOffset = commandNum;
        if(commandNum >= optionScrollOffset + maxVisible) optionScrollOffset = commandNum - maxVisible + 1;

        textX=frameX+gp.tileSize;
        textY+= (int)(gp.tileSize*1.5);

        for(int visualIndex=0; visualIndex<maxVisible; visualIndex++){
            int actualIndex = optionScrollOffset + visualIndex;
            if(actualIndex>=labels.size()) break;
            String txt = labels.get(actualIndex);
            if(txt.equals("Back")){
                textY+=gp.tileSize;
            }
            g2.drawString(txt,textX,textY);
            if(commandNum==actualIndex){
                g2.drawImage(gp.player.rightImages[0], textX - 45, textY - 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(gp.player.leftImages[0], textX-5+g2.getFontMetrics().stringWidth(txt), textY-30, gp.tileSize, gp.tileSize, null);
                if(gp.keyHandler.enterPressed){
                    switch(actualIndex){
                        case 2 -> { subState=1; commandNum=0; controlScrollOffset=0; optionScrollOffset=0; }
                        case 3 -> { subState=2; commandNum=0; optionScrollOffset=0; }
                        case 4 -> { gp.gameState=gp.keyHandler.previousGameState; commandNum=0; optionScrollOffset=0; }
                    }
                }
            }

            textY+=(int)(gp.tileSize*1.5);
        }

        g2.setStroke(new BasicStroke(3));
        int barX=frameX+(int)(gp.tileSize*5.5);
        int musicBarY=frameY+gp.tileSize*2;
        int seBarY=musicBarY+(int)(gp.tileSize*1.5);
        g2.drawRect(barX,musicBarY,120,24);
        g2.fillRect(barX,musicBarY,24*gp.music.volumeScale,24);
        g2.drawRect(barX,seBarY,120,24);
        g2.fillRect(barX,seBarY,24*gp.se.volumeScale,24);

        gp.config.saveConfig();
        gp.keyHandler.enterPressed=false;
    }

    public int getOptionMenuItemCount(){ return 5; }

    public void optionsControl(int frameX, int frameY){
        int textX;
        int textY;

        String text = "Options";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        g2.setFont(sized(12f));

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

        g2.setFont(sized(12f));
        String text="Abandon ship, ye coward!\nEven the rum can’t save you.\nAre you sure captain?";
        int availableWidth = frameX + (gp.tileSize * 8);
        ArrayList<String> wrappedLines = wrapText(text, availableWidth);
        for (String line : wrappedLines) {
            g2.drawString(line, textX, textY);
            textY += gp.tileSize;
        }

        g2.setFont(sized(24f));

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

    private String buildInventorySignature(Entity entity){
        sigBuilder.setLength(0);
        if(entity.inventory.isEmpty()) return "empty";
        for(int i=0;i<entity.inventory.size();i++){
            Entity it = entity.inventory.get(i);
            if(it==null){
                sigBuilder.append('n').append(i).append(';');
                continue;
            }
            sigBuilder.append(it.name).append(':').append(it.amount);
            if(entity==gp.player && (
                    it==entity.currentWeapon || it==entity.currentShield || it==entity.currentBoots ||
                            it==entity.currentChest || it==entity.currentHelmet)){
                sigBuilder.append(':').append('E');
            }
            sigBuilder.append('|');
        }
        sigBuilder.append('#').append(entity.inventory.size());
        return sigBuilder.toString();
    }

    private BufferedImage getInventoryBaseImage(Entity entity,int frameWidth,int frameHeight){
        if(!ENABLE_UI_CACHING) return null;
        InventoryCache cache = inventoryCaches.get(entity);
        String sig = buildInventorySignature(entity);
        if(cache!=null && cache.width==frameWidth && cache.height==frameHeight && sig.equals(cache.signature)){
            return cache.image;
        }
        BufferedImage img = new BufferedImage(frameWidth,frameHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setComposite(AlphaComposite.SrcOver);
        final int slotXStart=20;
        final int slotYStart=20;
        int slotX=slotXStart;
        int slotY=slotYStart;
        int slotSize=gp.tileSize+3;
        g.setFont(sized(12f));
        for(int i=0;i<entity.inventory.size();i++){
            Entity item = entity.inventory.get(i);
            if(item==null){
                slotX+=slotSize;
                if(i%5==4){ slotX=slotXStart; slotY+=slotSize; }
                continue;
            }
            if(entity==gp.player && (item==entity.currentWeapon || item==entity.currentShield || item==entity.currentBoots || item==entity.currentChest || item==entity.currentHelmet)){
                g.setColor(new Color(98,189,38,139));
                g.fillRoundRect(slotX,slotY,gp.tileSize,gp.tileSize,10,10);
            }
            g.drawImage(item.down1,slotX,slotY,null);
            if((entity==gp.player || entity==chest) && item.amount>1){
                String s = String.valueOf(item.amount);
                g.setColor(new Color(60,60,60));
                int amountX = getXAlignToRightTextOffscreen(g,s,slotX+44);
                int amountY = slotY+gp.tileSize;
                g.drawString(s,amountX,amountY);
                g.setColor(Color.white);
                g.drawString(s,amountX-3,amountY-3);
            }
            slotX+=slotSize;
            if(i%5==4){ slotX=slotXStart; slotY+=slotSize; }
        }
        g.dispose();
        if(cache==null){ cache=new InventoryCache(); inventoryCaches.put(entity,cache); }
        cache.signature=sig;
        cache.image=img;
        cache.width=frameWidth; cache.height=frameHeight;
        return img;
    }

    private int getXAlignToRightTextOffscreen(Graphics2D g,String text,int tailX){
        int length = (int) g.getFontMetrics().getStringBounds(text,g).getWidth();
        return tailX-length;
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
        }else if(entity==chest){
            slotTCol=chestSlotCol;
            slotTRow=chestSlotRow;
            frameX = gp.tileSize * 2;
        }
        else{
            slotTCol=tradeSlotCol;
            slotTRow=tradeSlotRow;
            frameX = gp.tileSize * 2;
        }
         drawSubWindow(frameX,frameY,frameWidth,frameHeight);

         BufferedImage base = getInventoryBaseImage(entity,frameWidth,frameHeight);
         if(base!=null){
             g2.drawImage(base,frameX,frameY,null);
         } else {
             final int slotXStart=frameX+20;
             final int slotYStart=frameY+20;
             int slotX=slotXStart;
             int slotY=slotYStart;
             int slotSize=gp.tileSize+3;
             for(int i=0;i<entity.inventory.size();i++) {
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
                     if((entity==gp.player || entity==chest )&& entity.inventory.get(i).amount>1) {
                         g2.setFont(sized(12f));
                         int amountX;
                         int amountY;
                         String s=""+entity.inventory.get(i).amount;
                         amountX=getXAlignToRightText(s,slotX+44);
                         amountY=slotY+gp.tileSize;
                         g2.setColor(new Color(60, 60, 60));
                         g2.drawString(s, amountX, amountY);
                         g2.setColor(Color.white);
                         g2.drawString(s, amountX-3, amountY-3);
                     }
                     slotX += slotSize;

                     if (i % 5 == 4) {
                         slotX = slotXStart;
                         slotY += slotSize;
                     }
                 }
             }
         }

         //cursor (dynamic overlay always drawn)
        if(cursor) {
            final int slotXStart=frameX+20;
            final int slotYStart=frameY+20;
            int slotSize=gp.tileSize+3;
            int cursorX=slotXStart+(slotSize*slotTCol);
            int cursorY=slotYStart+(slotSize*slotTRow);
            int cursorWidth=gp.tileSize;
            int cursorHeight=gp.tileSize;
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight,10,10);

            // description
            int dFrameY=frameY+frameHeight+gp.tileSize;
            int dFrameHeight=gp.tileSize*5;
            int textX= frameX +20;
            int textY=dFrameY+gp.tileSize;
            g2.setFont(sized(10f));

            int itemIndex=getItemIndexSlot(slotTCol,slotTRow);

            if(itemIndex<entity.inventory.size()){

                drawSubWindow(frameX,dFrameY, frameWidth,dFrameHeight);

                ArrayList<String> wrappedLines = wrapText(entity.inventory.get(itemIndex).itemDescription, frameWidth);

                for (String line : wrappedLines) {
                    line = gp.drinkSystem.slurIfNeeded(line);
                    g2.drawString(line, textX, textY);
                    textY += 40;
                }
                if(entity==gp.player && gp.gameState==gp.characterState) {
                    g2.setFont(sized(12f));
                    if (entity.inventory.get(itemIndex) != null) {
                        if (gp.player.inventory.get(itemIndex).gearType > 1) {
                            drawSubWindow((int) (gp.tileSize * 12.8), gp.tileSize * 6, (int) (gp.tileSize * 6.4), gp.tileSize);
                            g2.drawString("Press ENTER to use", (int) (gp.tileSize * 13.6), (int) (gp.tileSize * 6.6));
                        }
                       else if (!gp.player.isEquipped(gp.player.inventory.get(itemIndex))) {
                            drawSubWindow((int) (gp.tileSize * 12.8), gp.tileSize * 6, (int) (gp.tileSize * 6.4), gp.tileSize);
                            g2.drawString("Press ENTER to equip", (int) (gp.tileSize * 13.5), (int) (gp.tileSize * 6.6));
                        } else if (gp.player.isEquipped(gp.player.inventory.get(itemIndex))) {
                            drawSubWindow((int) (gp.tileSize * 12.8), gp.tileSize * 6, (int) (gp.tileSize * 6.4), gp.tileSize);
                            g2.drawString("Press ENTER to unequip", (int) (gp.tileSize * 13.2), (int) (gp.tileSize * 6.6));
                        }
                    }
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

        String[] manualLines = text.split("\n");

        for (String line : manualLines) {
            if (g2.getFontMetrics().stringWidth(line) <= maxWidth) {
                wrappedLines.add(line);
            } else {
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
        if(ENABLE_UI_CACHING){
            BufferedImage cached = getSubWindowBuffered(width,height);
            g2.drawImage(cached,x,y,null);
            return;
        }
        Color c = new Color(0, 0, 0, 175);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255, 200);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    private BufferedImage getSubWindowBuffered(int width,int height){
        long key = (((long)width)<<32) ^ (height & 0xffffffffL);
        BufferedImage img = subWindowCache.get(key);
        if(img!=null) return img;
        img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setComposite(AlphaComposite.SrcOver);
        Color fill = new Color(0,0,0,175);
        g.setColor(fill);
        g.fillRoundRect(0,0,width,height,35,35);
        Color border = new Color(255,255,255,200);
        g.setColor(border);
        g.setStroke(new BasicStroke(5));
        g.drawRoundRect(5,5,width-10,height-10,25,25);
        g.dispose();
        subWindowCache.put(key,img);
        return img;
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

        int totalHearts = 6;

        int scaledHealth = (int)Math.round(((double)gp.player.health / gp.player.maxHealth) * totalHearts);

        for (int i = 0; i < totalHearts; i += 2) {
            g2.drawImage(heartEmpty, x, y, null);
            x += gp.tileSize;
        }

        x = gp.tileSize / 2;

        for (int i = 0; i < scaledHealth; ) {
            g2.drawImage(heartHalf, x, y, null);
            i++;
            if (i < scaledHealth) {
                g2.drawImage(heartFull, x, y, null);
                i++;
            }
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
        g2.setFont(sized(10f));

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
    private boolean transferItemToChest(Entity item, int playerItemIndex) {
        if (item.stackable) {
            int chestIndex = chest.searchItemInInventory(item.name);
            if (chestIndex != 999) {
                chest.inventory.get(chestIndex).amount++;
                if(gp.player.inventory.get(playerItemIndex).amount>1){
                    gp.player.inventory.get(playerItemIndex).amount--;
                }
                else {
                    gp.player.inventory.remove(playerItemIndex);
                }
                return true;
            } else {
                if (chest.inventory.size() < chest.maxInventorySize) {
                    try {
                        Entity newItem=item.getClass().getConstructor(GamePanel.class).newInstance(gp);
                        newItem.amount=1;
                        chest.inventory.add(newItem);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }

                    if(gp.player.inventory.get(playerItemIndex).amount>1){
                        gp.player.inventory.get(playerItemIndex).amount--;
                    }else{
                        gp.player.inventory.remove(playerItemIndex);
                    }
                    return true;
                }
            }
        } else {
            if (chest.inventory.size() < chest.maxInventorySize) {
                chest.inventory.add(item);
                gp.player.inventory.remove(playerItemIndex);
                return true;
            }
        }
        return false;
    }

    private void cacheFonts(){
        Font base = PublicPixel != null ? PublicPixel : new Font("Arial", Font.PLAIN, 12);
        font10 = base.deriveFont(Font.PLAIN,10f);
        font12 = base.deriveFont(Font.PLAIN,12f);
        font15 = base.deriveFont(Font.PLAIN,15f);
        font20 = base.deriveFont(Font.PLAIN,20f);
        font24 = base.deriveFont(Font.PLAIN,24f);
        font48 = base.deriveFont(Font.PLAIN,48f);
        font64 = base.deriveFont(Font.PLAIN,64f);
        font68 = base.deriveFont(Font.PLAIN,68f);
    }

    private Font sized(float sz){
        if(font10==null) cacheFonts();
        if(sz<=10f) return font10;
        if(sz<=12f) return font12;
        if(sz<=15f) return font15;
        if(sz<=20f) return font20;
        if(sz<=24f) return font24;
        if(sz<=48f) return font48;
        if(sz<=64f) return font64;
        return font68 != null ? font68 : (PublicPixel!=null?PublicPixel: new Font("Arial",Font.PLAIN,(int)sz));
    }
}
