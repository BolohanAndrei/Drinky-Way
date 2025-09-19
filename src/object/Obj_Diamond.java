package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Diamond extends Entity {
    GamePanel gp;
    public static final String objName="Diamond";
    public Obj_Diamond(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name=objName;
        down1=setup("/objects/diamond_smooth");
        value=40;
    }
    public boolean use(Entity e){

        gp.se.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
        return true;
    }
}
