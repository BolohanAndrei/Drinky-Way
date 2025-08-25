package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Amber extends Entity {
    GamePanel gp;
    public Obj_Amber(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name="Amber";
        down1=setup("/objects/amber");
        value=10;
    }
    public void use(Entity e){

        gp.sound.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
    }
}
