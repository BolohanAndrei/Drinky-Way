package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Cup extends Entity {
    GamePanel gp;
    public Obj_Cup(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name="Cup";
        down1=setup("/objects/cup");
        value=2;
    }
    public void use(Entity e){

        gp.sound.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
    }
}
