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
        boolean isCorrect=false;
        return  isCorrect;
    }
    public void playSE(){}
    public InteractiveTiles getDestroyedFrom(){
        InteractiveTiles tile=null;
        return  tile;
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

        g.drawImage(down1, screenX, screenY, gp.tileSize, gp.tileSize, null);

    }
}
