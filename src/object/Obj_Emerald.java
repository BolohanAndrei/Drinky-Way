package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Emerald extends Entity {
    GamePanel gp;
    public static final String objName="Emerald";
    public Obj_Emerald(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name=objName;
        down1=setup("/objects/emerald");
        value=30;
    }
    public boolean use(Entity e){

        gp.se.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
        return true;
    }
}
