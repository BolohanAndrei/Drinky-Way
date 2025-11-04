package tiles_interactive;

import Entity.Entity;
import Main.GamePanel;
import object.*;

import java.awt.*;

public class IT_Wall extends InteractiveTiles{
    GamePanel gp;
    public IT_Wall(GamePanel gp,int col,int row) {
        super(gp,col,row);
        this.gp=gp;
        collision = true;
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        this.x=gp.tileSize*col;
        this.y=gp.tileSize*row;
        down1=setup("tiles_interactive/destructiblewall");
        destructible=true;
        health=4;
    }
    public boolean isCorrectItem(Entity playerEntity) {
        return playerEntity != null
                && playerEntity.currentWeapon != null
                && playerEntity.currentWeapon instanceof Obj_Pickaxe;
    }
    public void playSE(){
        gp.se.playSE(36);
    }
    public InteractiveTiles getDestroyedFrom(){
        InteractiveTiles tile=null;
        return  tile;
    }

    public Color getParticleColor(){
        return new Color(83, 83, 83);
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
    public void checkDrop() {
        int roll = new java.util.Random().nextInt(100);

        if (roll <= 60) {
            dropItem(new Obj_Coin(gp));
        } else if (roll <= 70) {
            dropItem(new Obj_Heal_Potion(gp));
        } else if (roll <= 86) {
            dropItem(new Obj_Cup(gp));
        } else if (roll <= 88) {
            dropItem(new Obj_Amber(gp));
        } else if (roll <= 90) {
            dropItem(new Obj_Amethyst(gp));
        } else if (roll <= 92) {
            dropItem(new Obj_Diamond(gp));
        } else if (roll <= 94) {
            dropItem(new Obj_Emerald(gp));
        } else if (roll <= 96) {
            dropItem(new Obj_Quartz(gp));
        } else if (roll <= 98) {
            dropItem(new Obj_Ruby(gp));
        } else{
            dropItem(new Obj_Sapphire(gp));
        }
    }

}
