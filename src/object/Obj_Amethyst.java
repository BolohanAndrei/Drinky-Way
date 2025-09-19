package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Amethyst extends Entity {
    GamePanel gp;
    public static final String objName="Amethyst";
    public Obj_Amethyst(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name=objName;
        down1=setup("/objects/amethyst");
        value=15;
    }
    public boolean use(Entity e){

        gp.se.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
        return true;
    }
}
