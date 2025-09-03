package tiles_interactive;

import Entity.Entity;
import Main.GamePanel;

import java.awt.*;

public class InteractiveTiles extends Entity {
    GamePanel gp;
    public boolean destructible=false;
    public InteractiveTiles(GamePanel gp,int col,int row) {
        super(gp);
        this.gp=gp;

    }
    public boolean isCorrectItem(Entity item){
        return false;
    }
    public void playSE(){}
    public InteractiveTiles getDestroyedFrom(){
        return null;
    }
    public void update(){
        if(invincible){
            invincibleCounter++;
            if(invincibleCounter>20){
                invincibleCounter=0;
                invincible=false;
            }
        }
    }
    public void draw(Graphics2D g) {
        int screenX = x - gp.player.x + gp.player.screenX;
        int screenY = y - gp.player.y + gp.player.screenY;

        if (screenX + gp.tileSize > 0 && screenX < gp.screenWidth &&
            screenY + gp.tileSize > 0 && screenY < gp.screenHeight) {
            g.drawImage(down1, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
