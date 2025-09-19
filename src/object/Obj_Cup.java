package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Cup extends Entity {
    GamePanel gp;
    public static final String objName="Cup";
    public Obj_Cup(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name=objName;
        down1=setup("/objects/cup");
        value=2;
    }
    public boolean use(Entity e){

        gp.se.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
        return true;
    }
}
