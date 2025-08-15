package Main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    EventRect[][] eventRect;

    int previousEventX, previousEventY;
    boolean canTouchEvent=true;


    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
        int col=0,row=0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 8;
            eventRect[col][row].y = 8;
            eventRect[col][row].width = 32;
            eventRect[col][row].height = 32;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }

        }

    }

    public void checkEvent() {
        int xDistance=Math.abs(gp.player.x-previousEventX);
        int yDistance=Math.abs(gp.player.y-previousEventY);
        int distance=Math.max(xDistance, yDistance);
        if(distance>gp.tileSize){
            canTouchEvent=true;
        }

        if(canTouchEvent) {
            if (hit(27, 16, "right")) {
                damagePit(27, 16, gp.dialogueState);}
            if (hit(23, 12, "up")) {
                healingEvent(23, 12, gp.dialogueState);
            }
            if (hit(25, 19, "right")) {
                teleportEvent(25, 19, gp.dialogueState);
            }
        }
    }

    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false;

        gp.player.solidArea.x = gp.player.x + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.y + gp.player.solidArea.y;
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].eventRectDefaultY;

        if (gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;
                previousEventX=gp.player.x;
                previousEventY=gp.player.y;
            }
        }

        // Reset the solid area position
        gp.player.solidArea.x =  gp.player.solidAreaDefaultX;
        gp.player.solidArea.y =  gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;

    }

    public void damagePit(int col,int row,int gameState) {
            gp.gameState = gameState;
            gp.playSE(18);
            gp.ui.currentDialogue = "Arrr! You fell into a pit!";
            gp.player.health -= 2;
            eventRect[col][row].eventDone=true;
            canTouchEvent=false;

    }

    public void teleportEvent(int col,int row,int gameState){
            gp.gameState = gameState;
            gp.ui.currentDialogue = "Arrr! Where I am?!";
            gp.player.x = gp.tileSize * 37;
            gp.player.y = gp.tileSize * 10;
            gp.player.direction = "down";
    }

    public void healingEvent(int col,int row,int gameState) {
        if(gp.keyHandler.ePressed){
            gp.playSE(12);
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Arrr! You found a healing drink!";
        gp.player.health =gp.player.maxHealth;
    }}

    public void resetEvent() {
       // eventRect.x = eventRectDefaultX;
        //eventRect.y = eventRectDefaultY;
    }

}
