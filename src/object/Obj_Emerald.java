package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Emerald extends Entity {
    GamePanel gp;
    public Obj_Emerald(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name="Emerald";
        down1=setup("/objects/emerald");
        value=30;
    }
    public void use(Entity e){

        gp.sound.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
    }
}
