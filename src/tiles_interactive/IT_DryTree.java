package tiles_interactive;

import Entity.Entity;
import Main.GamePanel;
import object.Obj_Axe;

import java.awt.*;

public class IT_DryTree extends InteractiveTiles{
    GamePanel gp;
    public IT_DryTree(GamePanel gp,int col,int row) {
        super(gp,col,row);
        this.gp=gp;
        collision = true;
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        this.x=gp.tileSize*col;
        this.y=gp.tileSize*row;
        down1=setup("/tiles_interactive/drytree");
        destructible=true;
        health=3;
    }
    public boolean isCorrectItem(Entity playerEntity) {
        return playerEntity != null
                && playerEntity.currentWeapon != null
                && playerEntity.currentWeapon instanceof Obj_Axe;
    }
    public void playSE(){
        gp.sound.playSE(26);
    }
    public InteractiveTiles getDestroyedFrom(){
        InteractiveTiles tile=new IT_Trunk(gp,x/gp.tileSize,y/gp.tileSize);
        return  tile;
    }

    public Color getParticleColor(){
        return new Color(65,50,30);
    }

    public int getParticleSize(){
        return 6;
    }

    public int getParticleSpeed()
    {
        return 1;
    }

    public int getParticleMaxHealth(){
        return 20;
    }
}
