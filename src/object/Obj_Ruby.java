package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Ruby extends Entity {
    GamePanel gp;
    public static final String objName="Ruby";
    public Obj_Ruby(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name=objName;
        down1=setup("/objects/ruby");
        value=25;
    }
    public boolean use(Entity e){

        gp.se.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
        return true;
    }
}
