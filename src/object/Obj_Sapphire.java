package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Sapphire extends Entity {
    GamePanel gp;
    public static final String objName="Sapphire";
    public Obj_Sapphire(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name=objName;
        down1=setup("/objects/sapphire");
        value=20;
    }
    public boolean use(Entity e){

        gp.se.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
        return true;
    }
}
