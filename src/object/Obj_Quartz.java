package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Quartz extends Entity {
    GamePanel gp;
    public Obj_Quartz(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name="Quartz";
        down1=setup("/objects/quartz");
        value=5;
    }
    public void use(Entity e){

        gp.sound.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
    }
}
