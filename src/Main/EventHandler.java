package Main;

public class EventHandler {
    GamePanel gp;
    EventRect[][][] eventRect;
    int tempMap,tempRow, tempCol;

    int previousEventX, previousEventY;
    boolean canTouchEvent=true;


    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        int map=0;
        int col=0,row=0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow && map<gp.maxMap) {
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 8;
            eventRect[map][col][row].y = 8;
            eventRect[map][col][row].width = 32;
            eventRect[map][col][row].height = 32;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
                if(row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
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
            if (hit(0,27, 16, "any")) {
                damagePit( gp.dialogueState);}
           else if (hit(0,23, 12, "any")) {
                healingEvent( gp.dialogueState);
            }
            else if (hit(0,25, 19, "any")) {
                teleportEvent( gp.dialogueState,0,37,10);
            }
            else if(hit(0,10,39,"any")){
                teleportEvent( gp.dialogueState,1,12,13);
            }
            else if(hit(1,12,13,"any")){
                teleportEvent( gp.dialogueState,0,10,39);
            }
        }
    }

    public boolean hit(int map,int col, int row, String reqDirection) {
        boolean hit = false;

        if(map==gp.currentMap){
            gp.player.solidArea.x = gp.player.x + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.y + gp.player.solidArea.y;
            eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].eventRectDefaultY;

            if (gp.player.solidArea.intersects(eventRect[map][col][row]) && !eventRect[map][col][row].eventDone) {
                if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;
                    previousEventX=gp.player.x;
                    previousEventY=gp.player.y;
                }
            }

            gp.player.solidArea.x =  gp.player.solidAreaDefaultX;
            gp.player.solidArea.y =  gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;

        }

        return hit;

    }

    public void damagePit(int gameState) {
        if(gp.keyHandler.ePressed) {
            gp.gameState = gameState;
            gp.se.playSE(18);
            gp.ui.currentDialogue = "Arrrgh! My legs be softer than I thought… blasted hole stole my health!";
            gp.player.health -= 2;
            if (gp.player.health <= 0) {
                gp.player.health = 0;
            }
        }
    }

    public void teleportEvent(int gameState,int map,int col,int row){

        gp.gameState = gp.transitionState;
        tempMap=map;
        tempRow=row;
        tempCol=col;
        gp.se.playSE(3);
        canTouchEvent=false;
    }

    public void healingEvent(int gameState) {
        if(gp.keyHandler.ePressed){
            gp.se.playSE(12);
        gp.gameState = gameState;
        gp.ui.currentDialogue = "By the seas! The siren’s brew heals my wounds… and curses me with sobriety!";
            gp.player.health =gp.player.maxHealth;
        gp.drinkSystem.soberUp(gp.player);
    }}


}
